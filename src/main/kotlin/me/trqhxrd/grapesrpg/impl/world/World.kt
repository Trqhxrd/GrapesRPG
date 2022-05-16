package me.trqhxrd.grapesrpg.impl.world

import me.trqhxrd.grapesrpg.impl.world.loading.ChunkLoader
import me.trqhxrd.grapesrpg.impl.world.loading.ChunkSaver
import me.trqhxrd.grapesrpg.util.coords.ChunkID
import me.trqhxrd.grapesrpg.util.coords.Coordinate
import me.trqhxrd.grapesrpg.util.sync.ReadWriteMutex
import org.bukkit.Location
import org.bukkit.event.world.WorldEvent
import org.jetbrains.exposed.sql.Database
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import me.trqhxrd.grapesrpg.api.world.Chunk as ChunkAPI
import me.trqhxrd.grapesrpg.api.world.World as WorldAPI
import org.bukkit.World as BukkitWorld

data class World(
    override val bukkitWorld: BukkitWorld,
    override val name: String,
    override val loadedChunks: MutableMap<ChunkID, ChunkAPI> = ConcurrentHashMap(),
    private val database: Database =
        Database.connect("jdbc:sqlite://${bukkitWorld.worldFolder.absolutePath}/grapes.sqlite?foreign_keys=on"),
    private val dbLock: ReadWriteMutex = ReadWriteMutex(),
    override val loader: ChunkLoader = ChunkLoader(database, dbLock),
    override val saver: ChunkSaver = ChunkSaver(database, dbLock)
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

    override fun getChunk(id: ChunkID): ChunkAPI = if (this.chunkExists(id)) this.loadedChunks[id]!!
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
        val chunk = this.addChunk(id)
        this.loader.add(chunk)

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

    override fun getBlock(id: Coordinate) = this.getChunk(id.toChunkID()).getBlock(id.inChunkCoords())

    override fun getBlock(loc: Location) = this.getBlock(Coordinate(loc))

    override fun save() = this.loadedChunks.values.forEach { this.saver.add(it) }
}
