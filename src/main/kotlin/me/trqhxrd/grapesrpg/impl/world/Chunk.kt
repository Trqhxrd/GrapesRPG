package me.trqhxrd.grapesrpg.impl.world

import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import me.trqhxrd.grapesrpg.api.world.World
import me.trqhxrd.grapesrpg.impl.world.blockdata.Void
import me.trqhxrd.grapesrpg.util.coords.ChunkID
import me.trqhxrd.grapesrpg.util.coords.Coordinate
import me.trqhxrd.grapesrpg.api.world.Block as BlockAPI
import me.trqhxrd.grapesrpg.api.world.Chunk as ChunkAPI

class Chunk(
    override val id: ChunkID,
    override val world: World,
) : ChunkAPI {

    private val lock = Mutex()
    private val blocks: MutableMap<Coordinate, BlockAPI> = mutableMapOf()

    override fun getBlockRelative(coords: Coordinate): BlockAPI {
        return runBlocking {
            return@runBlocking this@Chunk.getBlockRelativeAsync(coords).await()
        }
    }

    override suspend fun getBlockRelativeAsync(coords: Coordinate) = WorldScope.async {
        this@Chunk.lock.withLock(this@Chunk) {
            return@async this@Chunk.getBlockRelativeUnsafe(coords)
        }
    }

    private fun getBlockRelativeUnsafe(coords: Coordinate): BlockAPI {
        return if (this@Chunk.blocks.containsKey(coords)) this@Chunk.blocks[coords]!!
        else this@Chunk.addBlockUnsafe(coords)
    }

    override fun getBlock(coords: Coordinate) = runBlocking {
        return@runBlocking this@Chunk.getBlockAsync(coords).await()
    }

    override suspend fun getBlockAsync(coords: Coordinate) = WorldScope.async {
        this@Chunk.lock.withLock {
            return@async this@Chunk.getBlockUnsafe(coords)
        }
    }

    private fun getBlockUnsafe(coords: Coordinate): BlockAPI {
        val chunkID = coords.toChunkID()
        if (chunkID != this.id) throw IllegalArgumentException("Can only request block from within chunk!")
        val offset = coords.inChunkCoords()
        var block = this@Chunk.blocks[offset]
        if (block == null) block = this@Chunk.addBlockUnsafe(offset)

        return block
    }

    override fun getBlocks(): Map<Coordinate, BlockAPI> {
        return runBlocking {
            this@Chunk.getBlocksAsync().await()
        }
    }

    override suspend fun getBlocksAsync() = WorldScope.async {
        this@Chunk.lock.withLock(this@Chunk) {
            return@async this@Chunk.getBlocksUnsafe()
        }
    }

    private fun getBlocksUnsafe() = this.blocks.toMap()

    private fun addBlockUnsafe(coords: Coordinate): BlockAPI {
        return if (this@Chunk.containsBlockUnsafe(coords)) this@Chunk.getBlockUnsafe(coords)
        else {
            val block = Block(Coordinate(this@Chunk.id, coords), this@Chunk, Void())
            this@Chunk.blocks[coords.inChunkCoords()] = block
            block
        }
    }

    private fun containsBlockUnsafe(coords: Coordinate) = this.blocks.containsKey(coords)
}
