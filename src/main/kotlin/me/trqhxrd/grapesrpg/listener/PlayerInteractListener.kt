package me.trqhxrd.grapesrpg.listener

import me.trqhxrd.grapesrpg.GrapesRPG
import me.trqhxrd.grapesrpg.impl.world.World
import me.trqhxrd.grapesrpg.util.AbstractListener
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot

class PlayerInteractListener : AbstractListener(GrapesRPG.plugin) {

    @EventHandler(priority = EventPriority.LOW)
    fun onPlayerInteract(e: PlayerInteractEvent) {
        println(1)
        if (e.hand == EquipmentSlot.OFF_HAND) return
        World.getWorld(e.clickedBlock!!.world)
            .getBlock(e.clickedBlock!!.location)
            .data.onClick(e)
    }
}
