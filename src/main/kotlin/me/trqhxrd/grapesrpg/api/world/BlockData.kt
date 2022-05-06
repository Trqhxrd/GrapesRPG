package me.trqhxrd.grapesrpg.api.world

import org.bukkit.Material
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.player.PlayerInteractEvent

interface BlockData {
    val type: Material

    fun onClick(event: PlayerInteractEvent)
    fun onBreak(event: BlockBreakEvent)
    fun onPlace(event: BlockPlaceEvent)

    fun save()
    fun load()
}
