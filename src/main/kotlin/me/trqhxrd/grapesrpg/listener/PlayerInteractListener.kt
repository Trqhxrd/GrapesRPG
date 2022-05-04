package me.trqhxrd.grapesrpg.listener

import me.trqhxrd.grapesrpg.GrapesRPG
import me.trqhxrd.grapesrpg.gui.crafting.CraftingGUI
import me.trqhxrd.grapesrpg.util.AbstractListener
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

class PlayerInteractListener : AbstractListener(GrapesRPG.plugin) {

    @EventHandler
    fun onPlayerInteract(e: PlayerInteractEvent) {
        if (e.action == Action.RIGHT_CLICK_BLOCK) {
            if (e.clickedBlock!!.type == Material.CRAFTING_TABLE) {
                CraftingGUI(e.player)
                e.isCancelled = true
            }
        }
    }
}
