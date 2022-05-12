package me.trqhxrd.grapesrpg.api.world.jdbc

import me.trqhxrd.grapesrpg.GrapesRPG
import me.trqhxrd.grapesrpg.util.coords.ChunkID
import org.jetbrains.exposed.dao.id.IdTable

class ChunkTable(id: ChunkID) : IdTable<String>(genTableName(id)) {
    override val id = varchar("id", 256).entityId()
    val dataType = varchar("type", 256)
    val data = varchar("block", GrapesRPG.plugin.config.getInt("jdbc.block_max_length"))

    companion object {
        fun genTableName(id: ChunkID) = "CHUNK_${id.x}_${id.z}"
    }
}
