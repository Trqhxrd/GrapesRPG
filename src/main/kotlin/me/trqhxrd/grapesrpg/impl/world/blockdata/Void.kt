package me.trqhxrd.grapesrpg.impl.world.blockdata

import me.trqhxrd.grapesrpg.util.ModuleKey
import org.bukkit.Material
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.player.PlayerInteractEvent

class Void : BlockData<Void>(KEY, Material.AIR) {

    companion object {
        val KEY = ModuleKey("grapes", "void")
    }

    override fun onClick(event: PlayerInteractEvent) {
    }

    override fun onBreak(event: BlockBreakEvent) {}
    override fun onPlace(event: BlockPlaceEvent) {}
}
