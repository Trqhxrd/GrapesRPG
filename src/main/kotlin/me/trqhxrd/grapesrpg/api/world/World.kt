package me.trqhxrd.grapesrpg.api.world

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import me.trqhxrd.grapesrpg.util.coords.ChunkID
import me.trqhxrd.grapesrpg.util.coords.Coordinate

interface World {
    val name: String
    val bukkit: org.bukkit.World

    fun getBlock(coordinate: Coordinate): Block
    fun getChunk(id: ChunkID): Chunk

    fun loadChunkAsync(id: ChunkID): Deferred<Chunk>
    suspend fun unloadChunkAsync(id: ChunkID): Job

    fun save(): Job
}
