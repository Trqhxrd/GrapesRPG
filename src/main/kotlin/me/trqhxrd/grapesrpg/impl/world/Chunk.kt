package me.trqhxrd.grapesrpg.impl.world

import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import me.trqhxrd.grapesrpg.api.world.World
import me.trqhxrd.grapesrpg.impl.world.blockdata.Void
import me.trqhxrd.grapesrpg.impl.world.jdbc.ChunkTable
import me.trqhxrd.grapesrpg.util.coords.ChunkID
import me.trqhxrd.grapesrpg.util.coords.Coordinate
import me.trqhxrd.grapesrpg.api.world.Block as BlockAPI
import me.trqhxrd.grapesrpg.api.world.Chunk as ChunkAPI

class Chunk(override val id: ChunkID, override val world: World) : ChunkAPI {

    val table = ChunkTable(id)
    private val lock = Mutex()
    private val blocks: MutableMap<Coordinate, BlockAPI> = mutableMapOf()

    override fun getBlockRelative(location: Coordinate): BlockAPI {
        return runBlocking {
            this@Chunk.lock.withLock {
                return@runBlocking this@Chunk.blocks[location]!!
            }
        }
    }

    override fun getBlock(location: Coordinate): BlockAPI {
        var block: BlockAPI?
        val chunkID = location.toChunkID()
        if (chunkID != this.id) throw IllegalArgumentException("Can only request block from within chunk!")
        val offset = location.inChunkCoords()
        runBlocking {
            this@Chunk.lock.withLock {
                block = this@Chunk.blocks[offset]
                println("getting block   $block")
                println("blockmap   $blocks")
            }
        }
        if (block == null) {
            block = this@Chunk.addBlock(offset)
        }
        return block!!
    }

    override fun getBlocks(): Map<Coordinate, BlockAPI> {
        return runBlocking {
            this@Chunk.lock.withLock {
                val map = this@Chunk.blocks.toMap()
                println("In map   ${this@Chunk.blocks}")
                println("getBlocks   $map")
                return@runBlocking map
            }
        }
    }

    private fun addBlock(coords: Coordinate): BlockAPI {
        val block: BlockAPI
        runBlocking {
            this@Chunk.lock.withLock {
                println("adding block")
                block = if (this@Chunk.blocks.containsKey(coords.inChunkCoords())) this@Chunk.getBlockRelative(coords.inChunkCoords())
                else Block(Coordinate(this@Chunk.id, coords), this@Chunk, Void())
                this@Chunk.blocks[coords] = block
                println("addBlock map   $blocks")
            }
        }
        return block
    }
}
