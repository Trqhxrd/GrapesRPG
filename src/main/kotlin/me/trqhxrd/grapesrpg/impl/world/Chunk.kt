package me.trqhxrd.grapesrpg.impl.world

import me.trqhxrd.grapesrpg.api.world.jdbc.ChunkTable
import me.trqhxrd.grapesrpg.util.coords.ChunkID
import me.trqhxrd.grapesrpg.util.coords.Coordinate
import org.bukkit.Chunk
import org.bukkit.Location
import java.util.concurrent.ConcurrentHashMap
import me.trqhxrd.grapesrpg.api.world.Block as BlockAPI
import me.trqhxrd.grapesrpg.api.world.Chunk as ChunkAPI
import me.trqhxrd.grapesrpg.api.world.World as WorldAPI

class Chunk(
    override val id: ChunkID,
    override val world: WorldAPI,
    override val bukkitChunk: Chunk,
    override val blocks: MutableMap<Coordinate, BlockAPI> = ConcurrentHashMap(),
    override val table: ChunkTable = ChunkTable(id),
) : ChunkAPI {

    override fun getBlock(id: Coordinate): BlockAPI {
        return if (this@Chunk.exists(id)) this@Chunk.blocks[id]!!
        else this@Chunk.addBlock(id)
    }

    override fun getBlock(loc: Location) = this.getBlock(Coordinate(loc))

    private fun addBlock(id: Coordinate): BlockAPI {
        return if (!this.exists(id)) {
            val block = Block(id, this, this.world, id.block(this.world.bukkitWorld))
            this@Chunk.blocks[id] = block
            block
        } else this.getBlock(id)
    }

    private fun addBlock(loc: Location) = this.addBlock(Coordinate(loc))

    override fun exists(id: Coordinate) = this.blocks.containsKey(id.inChunkCoords())


    override fun exists(loc: Location) = this.exists(Coordinate(loc))

    override fun corner() = Coordinate(this.id.x * 16, 0, this.id.z * 16)

    override fun save() = this.world.saver.add(this)

    override fun load() = this.world.loader.add(this)
}
