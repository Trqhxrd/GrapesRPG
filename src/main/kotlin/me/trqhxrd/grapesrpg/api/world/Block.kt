package me.trqhxrd.grapesrpg.api.world

import me.trqhxrd.grapesrpg.util.coords.Coordinate

interface Block {
    val location: Coordinate
    val inChunkLocation: Coordinate
    val chunk: Chunk
    val world: World
    var blockData: BlockData<*>
}
