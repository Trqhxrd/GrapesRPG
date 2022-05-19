package me.trqhxrd.grapesrpg.impl.world.jdbc

import me.trqhxrd.grapesrpg.api.world.Chunk
import me.trqhxrd.grapesrpg.util.coords.ChunkID
import org.jetbrains.exposed.dao.id.IdTable

class ChunkTable(chunkId: ChunkID) : IdTable<String>("CHUNK_${chunkId.x}_${chunkId.z}") {
    override val id = varchar("coordinate", 128).entityId()
    val block = varchar("block", 8192)

    constructor(chunk: Chunk) : this(chunk.id)
}
