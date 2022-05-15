package me.trqhxrd.grapesrpg.listener.player

import me.trqhxrd.grapesrpg.GrapesRPG
import me.trqhxrd.grapesrpg.game.item.attribute.Block
import me.trqhxrd.grapesrpg.impl.item.Item
import me.trqhxrd.grapesrpg.impl.world.World
import me.trqhxrd.grapesrpg.util.AbstractListener
import org.bukkit.Material
import org.bukkit.Tag
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack

class PlayerInteractListener : AbstractListener(GrapesRPG.plugin) {

    @EventHandler(priority = EventPriority.LOW)
    fun onPlayerInteract(e: PlayerInteractEvent) {
        if (e.clickedBlock == null) return
        if (e.hand == EquipmentSlot.OFF_HAND) {
            e.isCancelled = true
            return
        }
        val block = World.getWorld(e.clickedBlock!!.world).getBlock(e.clickedBlock!!.location)

        val b = block.blockData.onClick(e)
        e.isCancelled = b

        val loc =
            if (Tag.REPLACEABLE_PLANTS.isTagged(e.clickedBlock!!.type)) e.clickedBlock!!.location
            else e.clickedBlock!!.getRelative(e.blockFace).location

        val target = World.getWorld(e.clickedBlock!!.world).getBlock(loc)

        if (!b) {
            val item = e.item ?: ItemStack(Material.AIR)
            val i = if (Item.isGrapesItem(item)) Item.fromItemStack(item) else return
            val attr = i.getAttribute(Block::class) ?: return

            target.blockData = attr.blockData
        }

    }
}
