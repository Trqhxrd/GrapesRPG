package me.trqhxrd.grapesrpg.impl.world

import com.google.common.reflect.TypeToken
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import me.trqhxrd.grapesrpg.GrapesRPG
import me.trqhxrd.grapesrpg.impl.world.jdbc.ChunkTable
import me.trqhxrd.grapesrpg.util.coords.ChunkID
import me.trqhxrd.grapesrpg.util.coords.Coordinate
import me.trqhxrd.grapesrpg.util.sync.ReadWriteMutex
import org.bukkit.event.world.WorldEvent
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import me.trqhxrd.grapesrpg.api.world.Block as BlockAPI
import me.trqhxrd.grapesrpg.api.world.Chunk as ChunkAPI
import me.trqhxrd.grapesrpg.api.world.World as WorldAPI
import org.bukkit.World as BukkitWorld

class World(override val name: String, override val bukkit: BukkitWorld) : WorldAPI {

    private val chunks: MutableMap<ChunkID, ChunkAPI> = mutableMapOf()
    private val lock = Mutex()
    private val database = Database.connect("jdbc:sqlite://${bukkit.worldFolder.absolutePath}/grapes.sqlite")
    private val dbLock = ReadWriteMutex()

    init {
        addWorld(this)

        WorldScope.launch {
            for (x in -10..10)
                for (z in -10..10)
                    this@World.loadChunkAsync(ChunkID(x, z))
        }
    }

    companion object {
        private val worlds = mutableSetOf<World>()

        fun addWorld(world: World) = this.worlds.add(world)
        fun getWorld(world: BukkitWorld) = this.worlds.first { it.bukkit.uid == world.uid }
        fun getWorld(event: WorldEvent) = this.worlds.first { it.bukkit.uid == event.world.uid }
        fun removeWorld(world: World) = this.worlds.remove(world)
        fun containsWorld(e: WorldEvent) = this.worlds.firstOrNull { it.bukkit == e.world } != null
        fun containsWorld(world: BukkitWorld) = this.worlds.firstOrNull { it.bukkit == world } != null

        fun saveAll() {
            this.worlds.forEach {
                GrapesRPG.logger.info("Saving ${it.name}")
                runBlocking {
                    it.save().join()
                }
            }
        }

        fun disable() {
            this.saveAll()
            for (world in this.worlds.toSet()) this.removeWorld(world)
        }
    }

    override fun getBlock(coordinate: Coordinate) = runBlocking {
        return@runBlocking this@World.getBlockAsync(coordinate).await()
    }

    override suspend fun getBlockAsync(coordinate: Coordinate) = WorldScope.async {
        this@World.lock.withLock {
            return@async this@World.getBlockUnsafe(coordinate)
        }
    }

    private fun getBlockUnsafe(coordinate: Coordinate): BlockAPI {
        return this.getChunkUnsafe(coordinate.toChunkID()).getBlockRelative(coordinate.inChunkCoords())
    }

    override fun getChunk(id: ChunkID) = runBlocking {
        this@World.getChunkAsync(id).await()
    }

    override suspend fun getChunkAsync(id: ChunkID) = WorldScope.async {
        this@World.lock.withLock {
            return@async this@World.getChunkUnsafe(id)
        }
    }

    private fun getChunkUnsafe(id: ChunkID): ChunkAPI {
        return if (!this.chunks.containsKey(id)) this.addChunkUnsafe(id)
        else this.chunks[id]!!
    }

    override fun loadChunk(id: ChunkID) = runBlocking {
        return@runBlocking this@World.loadChunkAsync(id).await()
    }

    override suspend fun loadChunkAsync(id: ChunkID) = WorldScope.async {
        this@World.lock.withLock {
            this@World.dbLock.read {
                return@async this@World.loadChunkUnsafe(id)
            }
        }
    }

    private fun loadChunkUnsafe(id: ChunkID): ChunkAPI {
        return transaction(database) {
            val table = ChunkTable(id)
            val chunk = this@World.addChunkUnsafe(id)
            if (!table.exists()) return@transaction chunk
            table.selectAll().forEach {
                val blockData: BlockData<*> =
                    GrapesRPG.gson.fromJson(it[table.block], object : TypeToken<BlockData<*>>() {}.type)
                val blockRelative =
                    chunk.getBlockRelative(GrapesRPG.gson.fromJson(it[table.id].value, Coordinate::class.java))
                blockRelative.blockData = blockData
            }

            return@transaction chunk
        }
    }

    override fun unloadChunk(id: ChunkID) = runBlocking {
        this@World.unloadChunkAsync(id).join()
    }

    override suspend fun unloadChunkAsync(id: ChunkID) = WorldScope.launch {
        this@World.lock.withLock {
            this@World.dbLock.write {
                this@World.unloadChunkUnsafe(id)
            }
        }
    }

    private fun unloadChunkUnsafe(id: ChunkID) {
        val chunk = this@World.chunks[id]!!
        val blocks = chunk.getBlocks().filter { it.value.blockData !is me.trqhxrd.grapesrpg.impl.world.blockdata.Void }
        if (blocks.isEmpty()) return

        transaction(database) {
            val table = ChunkTable(chunk)
            if (!table.exists()) SchemaUtils.create(table)
            blocks.forEach { entry ->
                table.insert {
                    it[table.id] = entry.key.toJson()
                    it[table.block] = GrapesRPG.gson.toJson(entry.value.blockData)
                }
            }
        }
        this.chunks.remove(id)
    }

    override fun save() = WorldScope.launch {
        this@World.chunks.toMap().keys.forEach {
            this@World.unloadChunkAsync(it).join()
        }
    }

    private fun addChunkUnsafe(id: ChunkID): ChunkAPI {
        val chunk = Chunk(id, this)
        this.chunks[id] = chunk
        return chunk
    }
}
