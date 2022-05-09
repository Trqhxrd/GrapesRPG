package me.trqhxrd.grapesrpg.impl.world

import me.trqhxrd.grapesrpg.impl.world.jdbc.ChunkTable
import me.trqhxrd.grapesrpg.util.coords.ChunkID
import me.trqhxrd.grapesrpg.util.coords.Coordinate
import org.bukkit.Chunk
import org.bukkit.Location
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import me.trqhxrd.grapesrpg.api.world.Block as BlockAPI
import me.trqhxrd.grapesrpg.api.world.Chunk as ChunkAPI
import me.trqhxrd.grapesrpg.api.world.World as WorldAPI

class Chunk(
    override val id: ChunkID,
    override val world: WorldAPI,
    override val bukkitChunk: Chunk,
    override val blocks: MutableMap<Coordinate, BlockAPI> = mutableMapOf(),
    val table: ChunkTable = ChunkTable(id)
) : ChunkAPI {

    override fun getBlock(id: Coordinate) = if (this.exists(id)) this.blocks[id]!! else this.addBlock(id)

    override fun getBlock(loc: Location) = this.getBlock(Coordinate(loc))

    override fun addBlock(id: Coordinate): BlockAPI {
        return if (!this.exists(id)) {
            val b = Block(id, this, this.world, id.block(this.world.bukkitWorld))
            this.blocks[id] = b
            b
        } else this.getBlock(id)
    }

    override fun addBlock(loc: Location) = this.addBlock(Coordinate(loc))

    override fun exists(id: Coordinate) = this.blocks.containsKey(id.toChunkCoords())

    override fun exists(loc: Location) = this.exists(Coordinate(loc))

    override fun corner() = Coordinate(this.id.x * 16, 0, this.id.z * 16)

    override fun save(){
        println(this.id)
        this.blocks.values.forEach { it.save() }
    }

    override fun load() {
        val chunk = ChunkTable(this.id)
        transaction {
            chunk.selectAll()
                .map { it[chunk.id] }
                .map { Block(Coordinate(it.value), this@Chunk) }
                .forEach { it.load() }
        }
    }
}
