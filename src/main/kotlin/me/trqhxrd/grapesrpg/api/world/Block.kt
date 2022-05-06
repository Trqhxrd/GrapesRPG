package me.trqhxrd.grapesrpg.api.world

import org.bukkit.Location
import org.bukkit.block.Block as BukkitBlock

interface Block {
    val chunk: Chunk
    val world: World
    val bukkitBlock: BukkitBlock
    val location: Location
    var data: BlockData

    fun save()
    fun load()
}
