package me.trqhxrd.grapesrpg.api.world

import me.trqhxrd.grapesrpg.util.ModuleKey
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.player.PlayerInteractEvent

interface BlockData<T : BlockData<T>> {
    val id: ModuleKey

    fun onClick(event: PlayerInteractEvent)
    fun onBreak(event: BlockBreakEvent)
    fun onPlace(event: BlockPlaceEvent)

    fun save(): String
    fun load(serialized: String)
    fun apply(data: T)
}