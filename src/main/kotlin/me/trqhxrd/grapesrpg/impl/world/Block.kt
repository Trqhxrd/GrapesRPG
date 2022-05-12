package me.trqhxrd.grapesrpg.impl.world

import me.trqhxrd.grapesrpg.impl.world.blockdata.Void
import me.trqhxrd.grapesrpg.util.coords.Coordinate
import org.bukkit.block.Block
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

    override fun save() = this.chunk.save()

    override fun load() = this.chunk.load()
}
