package me.trqhxrd.grapesrpg.api.world

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import me.trqhxrd.grapesrpg.util.coords.ChunkID
import me.trqhxrd.grapesrpg.util.coords.Coordinate
import org.bukkit.World as BukkitWorld

interface World {
    val name: String
    val bukkit: BukkitWorld

    fun getBlock(coordinate: Coordinate): Block
    suspend fun getBlockAsync(coordinate: Coordinate): Deferred<Block>

    fun getChunk(id: ChunkID): Chunk
    suspend fun getChunkAsync(id: ChunkID): Deferred<Chunk>

    fun loadChunk(id: ChunkID): Chunk
    suspend fun loadChunkAsync(id: ChunkID): Deferred<Chunk>

    fun unloadChunk(id: ChunkID)
    suspend fun unloadChunkAsync(id: ChunkID): Job

    fun save(): Job
}
