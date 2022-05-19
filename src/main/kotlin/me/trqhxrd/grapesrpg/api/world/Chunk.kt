package me.trqhxrd.grapesrpg.api.world

import me.trqhxrd.grapesrpg.util.coords.ChunkID
import me.trqhxrd.grapesrpg.util.coords.Coordinate

interface Chunk {
    val id: ChunkID
    val world: World

    fun getBlockRelative(location: Coordinate): Block
    fun getBlock(location: Coordinate): Block
    fun getBlocks(): Map<Coordinate, Block>
}
