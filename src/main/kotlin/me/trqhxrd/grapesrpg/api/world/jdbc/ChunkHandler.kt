package me.trqhxrd.grapesrpg.api.world.jdbc

import kotlinx.coroutines.sync.Mutex
import me.trqhxrd.grapesrpg.api.world.Chunk
import me.trqhxrd.grapesrpg.util.sync.ReadWriteMutex
import org.jetbrains.exposed.sql.Database

interface ChunkHandler {
    val database: Database

    fun add(chunk: Chunk)
    suspend fun commit()

    fun shutdownGracefully()
}
