package me.trqhxrd.grapesrpg.api.world

import me.trqhxrd.grapesrpg.util.coords.Coordinate
import org.bukkit.block.Block as BukkitBlock

interface Block {
    val location: Coordinate
    val chunk: Chunk
    val world: World
    val bukkitBlock: BukkitBlock
    var data: BlockData<*>

    fun location(): Coordinate
    fun inChunkLocation(): Coordinate

    fun save()
    fun load()
}
