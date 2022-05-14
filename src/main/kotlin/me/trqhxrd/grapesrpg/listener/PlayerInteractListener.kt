package me.trqhxrd.grapesrpg.listener

import me.trqhxrd.grapesrpg.GrapesRPG
import me.trqhxrd.grapesrpg.impl.world.World
import me.trqhxrd.grapesrpg.util.AbstractListener
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.player.PlayerInteractEvent

class PlayerInteractListener : AbstractListener(GrapesRPG.plugin) {

    @EventHandler(priority = EventPriority.LOW)
    fun onPlayerInteract(e: PlayerInteractEvent) {
        val block = World.getWorld(e.clickedBlock!!.world).getBlock(e.clickedBlock!!.location)
        val b = block.blockData.onClick(e)

        if (!b) {
            Material.CRAFTING_TABLE
        }

        e.isCancelled = b
    }
}
