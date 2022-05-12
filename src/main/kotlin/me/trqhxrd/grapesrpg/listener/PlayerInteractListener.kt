package me.trqhxrd.grapesrpg.listener

import me.trqhxrd.grapesrpg.GrapesRPG
import me.trqhxrd.grapesrpg.game.item.attribute.Block
import me.trqhxrd.grapesrpg.impl.item.Item
import me.trqhxrd.grapesrpg.impl.world.World
import me.trqhxrd.grapesrpg.util.AbstractListener
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.player.PlayerInteractEvent

class PlayerInteractListener : AbstractListener(GrapesRPG.plugin) {

    @EventHandler(priority = EventPriority.LOW)
    fun onPlayerInteract(e: PlayerInteractEvent) {
        World.getWorld(e.clickedBlock!!.world)
            .getBlock(e.clickedBlock!!.location)
            .blockData.onClick(e)
        if (e.item != null) {
            val item = Item.fromItemStack(e.item!!)
            val block = item.getAttribute(Block::class)
            if (e.clickedBlock != null && block != null) {
                val b = e.clickedBlock!!.getRelative(e.blockFace)
                b.type = block.data.type
                World.getWorld(e.clickedBlock!!.world).getBlock(b.location).blockData = block.data
            }
        }
    }
}
