package me.trqhxrd.grapesrpg.impl.world

import com.google.gson.Gson
import me.trqhxrd.grapesrpg.impl.world.BlockData.Void
import me.trqhxrd.grapesrpg.impl.world.jdbc.ChunkTable
import me.trqhxrd.grapesrpg.util.ModuleKey
import me.trqhxrd.grapesrpg.util.coords.Coordinate
import org.bukkit.block.Block
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import me.trqhxrd.grapesrpg.api.world.Block as BlockAPI
import me.trqhxrd.grapesrpg.api.world.BlockData as BlockDataAPI
import me.trqhxrd.grapesrpg.api.world.Chunk as ChunkAPI
import me.trqhxrd.grapesrpg.api.world.World as WorldAPI

class Block(
    override val location: Coordinate,
    override val chunk: ChunkAPI,
    override val world: WorldAPI = chunk.world,
    override val bukkitBlock: Block = location.block(chunk.world.bukkitWorld),
    override var data: BlockDataAPI<*> = Void()
) : BlockAPI {

    override fun location() = Coordinate(
        this.chunk.id.x * 16 + this.location.x,
        this.location.y,
        this.chunk.id.z * 16 + this.location.z
    )

    override fun inChunkLocation() = this.location

    override fun save() {
        println(this.location)
        val chunk = ChunkTable(this.chunk.id)
        transaction {
            SchemaUtils.create(chunk)

            chunk.insert {
                it[id] = this@Block.location.toJson()
                it[dataType] = this@Block.data.id.serialized
                it[data] = Gson().toJson(this@Block.data.save())
            }
        }
    }

    override fun load() {
        val chunk = ChunkTable(this.chunk.id)
        transaction {
            SchemaUtils.create(chunk)

            val row = chunk.select { chunk.id.eq(this@Block.location.toJson()) }.first()
            val klass = BlockData.registry[ModuleKey.deserialize(row[chunk.dataType])]!!
            val data = klass.getConstructor().newInstance()
            data.load(row[chunk.data])
            this@Block.data = data
        }
    }
}
