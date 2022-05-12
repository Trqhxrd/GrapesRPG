package me.trqhxrd.grapesrpg.game.world.blockdata

import me.trqhxrd.grapesrpg.gui.crafting.CraftingGUI
import me.trqhxrd.grapesrpg.impl.world.blockdata.BlockData
import me.trqhxrd.grapesrpg.util.ModuleKey
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.player.PlayerInteractEvent

class CraftingTable : BlockData<CraftingTable>(KEY) {

    companion object {
        val KEY = ModuleKey("grapes", "crafting_table")
    }

    override fun onClick(event: PlayerInteractEvent) {
        if (event.action == Action.RIGHT_CLICK_BLOCK) CraftingGUI(event.player)
    }

    override fun onBreak(event: BlockBreakEvent) {}

    override fun onPlace(event: BlockPlaceEvent) {}
}
