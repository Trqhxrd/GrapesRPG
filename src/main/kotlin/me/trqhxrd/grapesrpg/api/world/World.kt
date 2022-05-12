package me.trqhxrd.grapesrpg.api.world

import me.trqhxrd.grapesrpg.api.world.jdbc.ChunkHandler
import me.trqhxrd.grapesrpg.util.coords.ChunkID
import me.trqhxrd.grapesrpg.util.coords.Coordinate
import org.bukkit.Location
import org.bukkit.World as BukkitWorld

interface World {
    val bukkitWorld: BukkitWorld
    val name: String
    val loadedChunks: MutableMap<ChunkID, Chunk>
    val loader: ChunkHandler
    val saver: ChunkHandler

    fun getChunk(id: ChunkID): Chunk
    fun getChunk(loc: Location): Chunk
    fun addChunk(id: ChunkID): Chunk
    fun addChunk(loc: Location): Chunk

    fun loadChunk(id: ChunkID): Chunk
    fun loadChunk(loc: Location): Chunk
    fun unloadChunk(id: ChunkID): Chunk
    fun unloadChunk(loc: Location): Chunk

    fun chunkExists(id: ChunkID): Boolean
    fun chunkExists(loc: Location): Boolean

    fun getBlock(id: Coordinate): Block
    fun getBlock(loc: Location): Block
    fun addBlock(id: Coordinate): Block
    fun addBlock(loc: Location): Block

    fun save()
}
