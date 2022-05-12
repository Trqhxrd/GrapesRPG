package me.trqhxrd.grapesrpg.impl.world

import kotlinx.coroutines.runBlocking
import me.trqhxrd.grapesrpg.api.world.jdbc.ChunkTable
import me.trqhxrd.grapesrpg.util.coords.ChunkID
import me.trqhxrd.grapesrpg.util.coords.Coordinate
import org.bukkit.Chunk
import org.bukkit.Location
import me.trqhxrd.grapesrpg.api.world.Block as BlockAPI
import me.trqhxrd.grapesrpg.api.world.Chunk as ChunkAPI
import me.trqhxrd.grapesrpg.api.world.World as WorldAPI

class Chunk(
    override val id: ChunkID,
    override val world: WorldAPI,
    override val bukkitChunk: Chunk,
    override val blocks: MutableMap<Coordinate, BlockAPI> = mutableMapOf(),
    override val table: ChunkTable = ChunkTable(id)
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

    override fun save() = runBlocking { this@Chunk.world.saver.add(this@Chunk) }

    override fun load() = runBlocking { this@Chunk.world.loader.add(this@Chunk) }
}
