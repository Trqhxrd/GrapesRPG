package me.trqhxrd.grapesrpg.api.world

import kotlinx.coroutines.Deferred
import me.trqhxrd.grapesrpg.impl.world.jdbc.ChunkTable
import me.trqhxrd.grapesrpg.util.coords.ChunkID
import me.trqhxrd.grapesrpg.util.coords.Coordinate

interface Chunk {
    val id: ChunkID
    val world: World

    fun getBlockRelative(coords: Coordinate): Block
    suspend fun getBlockRelativeAsync(coords: Coordinate): Deferred<Block>

    fun getBlock(coords: Coordinate): Block
    suspend fun getBlockAsync(coords: Coordinate): Deferred<Block>

    fun getBlocks(): Map<Coordinate, Block>
    suspend fun getBlocksAsync(): Deferred<Map<Coordinate, Block>>
}
