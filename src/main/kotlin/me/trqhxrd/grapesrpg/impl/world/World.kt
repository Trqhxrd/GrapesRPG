package me.trqhxrd.grapesrpg.impl.world

import com.google.common.reflect.TypeToken
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import me.trqhxrd.grapesrpg.GrapesRPG
import me.trqhxrd.grapesrpg.impl.world.jdbc.ChunkTable
import me.trqhxrd.grapesrpg.util.coords.ChunkID
import me.trqhxrd.grapesrpg.util.coords.Coordinate
import me.trqhxrd.grapesrpg.util.sync.ReadWriteMutex
import org.bukkit.event.world.WorldEvent
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.exists
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
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
            this.worlds
            this.worlds.forEach {
                runBlocking {
                    it.save().join()
                }
            }
        }
    }

    override fun getBlock(coordinate: Coordinate): BlockAPI {
        return this.getChunk(coordinate.toChunkID()).getBlock(coordinate)
    }

    override fun getChunk(id: ChunkID): ChunkAPI {
        return runBlocking {
            this@World.lock.withLock {
                if (!this@World.chunks.containsKey(id))
                    this@World.chunks[id] = Chunk(id, this@World)
                return@runBlocking this@World.chunks[id]!!
            }
        }
    }

    override fun loadChunkAsync(id: ChunkID): Deferred<ChunkAPI> = WorldScope.async {
        return@async this@World.dbLock.read {
            return@read transaction(database) {
                val table = ChunkTable(id)
                val chunk = Chunk(id, this@World)
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
    }

    override suspend fun unloadChunkAsync(id: ChunkID): Job = WorldScope.launch {
        this@World.dbLock.write {
            return@write transaction(database) {
                val table = ChunkTable(id)

                val chunk = Chunk(id, this@World)
                for (block in chunk.getBlocks()) {
                    println("insert   $block")
                    table.insert {
                        it[table.id] = block.key.toJson()
                        it[table.block] = GrapesRPG.gson.toJson(block.value.blockData)
                    }
                }
            }
        }
        this@World.lock.withLock { this@World.chunks.remove(id) }
    }

    override fun save() = WorldScope.launch {
        this@World.chunks.keys.forEach {
            this@World.unloadChunkAsync(it).join()
        }
    }
}
