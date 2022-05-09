package me.trqhxrd.grapesrpg.impl.world

import me.trqhxrd.grapesrpg.impl.world.jdbc.ChunkTable
import me.trqhxrd.grapesrpg.util.coords.ChunkID
import me.trqhxrd.grapesrpg.util.coords.Coordinate
import org.bukkit.Location
import org.bukkit.event.world.WorldEvent
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.exists
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*
import me.trqhxrd.grapesrpg.api.world.Chunk as ChunkAPI
import me.trqhxrd.grapesrpg.api.world.World as WorldAPI
import org.bukkit.World as BukkitWorld

class World(
    override val bukkitWorld: BukkitWorld,
    override val name: String,
    override val loadedChunks: MutableMap<ChunkID, ChunkAPI> = mutableMapOf(),
    val database: Database = Database.connect("jdbc:sqlite://${bukkitWorld.worldFolder.absolutePath}/grapes.sqlite")
) : WorldAPI {

    init {
        worlds.add(this)
    }

    companion object {
        val worlds = mutableSetOf<World>()

        fun getWorld(name: String) = worlds.first { it.name == name }
        fun getWorld(uid: UUID) = worlds.first { it.bukkitWorld.uid == uid }
        fun getWorld(world: BukkitWorld) = this.getWorld(world.name)
        fun getWorld(event: WorldEvent) = this.getWorld(event.world)
    }

    override fun getChunk(id: ChunkID): ChunkAPI =
        if (this.chunkExists(id)) this.loadedChunks[id]!!
        else this.loadChunk(id)

    override fun getChunk(loc: Location) = this.getChunk(ChunkID(loc))

    override fun addChunk(id: ChunkID): ChunkAPI {
        return if (!this.chunkExists(id)) {
            val chunk = Chunk(id, this, id.bukkitChunk(this.bukkitWorld))
            this.loadedChunks[id] = chunk
            chunk
        } else this.loadedChunks[id]!!
    }

    override fun addChunk(loc: Location) = this.addChunk(ChunkID(loc))

    override fun loadChunk(id: ChunkID): ChunkAPI {
        val chunk =  this.addChunk(id)
        transaction {
            val table = ChunkTable(id)
            if (table.exists()) {
                table.selectAll().forEach {
                    val coords = Coordinate(it[table.id].value)
                    val block = Block(coords, chunk)
                    block.load()
                    chunk.blocks[coords] = block
                }
            }
        }

        this.loadedChunks[id] = chunk
        return chunk
    }

    override fun loadChunk(loc: Location) = this.loadChunk(ChunkID(loc))

    override fun unloadChunk(id: ChunkID): ChunkAPI {
        val chunk = this.loadedChunks[id]!!
        chunk.save()
        this.loadedChunks.remove(id)
        return chunk
    }

    override fun unloadChunk(loc: Location) = this.unloadChunk(ChunkID(loc))

    override fun chunkExists(id: ChunkID) = this.loadedChunks.containsKey(id)

    override fun chunkExists(loc: Location) = this.chunkExists(ChunkID(loc))

    override fun getBlock(id: Coordinate) = this.getChunk(id.toChunkID()).getBlock(id.toChunkCoords())

    override fun getBlock(loc: Location) = this.getBlock(Coordinate(loc))

    override fun addBlock(id: Coordinate) = this.getChunk(id.toChunkID()).addBlock(id.toChunkCoords())

    override fun addBlock(loc: Location) = this.addBlock(Coordinate(loc))

    override fun save() {
        println(this.name)
        this.loadedChunks.values.forEach { it.save() }
    }
}