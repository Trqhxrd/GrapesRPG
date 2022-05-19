package me.trqhxrd.grapesrpg.impl.world

import me.trqhxrd.grapesrpg.api.world.BlockData
import me.trqhxrd.grapesrpg.api.world.Chunk
import me.trqhxrd.grapesrpg.api.world.World
import me.trqhxrd.grapesrpg.util.coords.Coordinate
import me.trqhxrd.grapesrpg.api.world.Block as BlockAPI

class Block(
    override val location: Coordinate,
    override val chunk: Chunk,
    override var blockData: BlockData<*>,
    override val inChunkLocation: Coordinate = location.inChunkCoords(),
    override val world: World = chunk.world
) : BlockAPI {
}
