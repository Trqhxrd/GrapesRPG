package me.trqhxrd.grapesrpg.listener.player

import me.trqhxrd.grapesrpg.GrapesRPG
import me.trqhxrd.grapesrpg.game.item.attribute.Block
import me.trqhxrd.grapesrpg.impl.item.Item
import me.trqhxrd.grapesrpg.impl.world.World
import me.trqhxrd.grapesrpg.util.AbstractListener
import me.trqhxrd.grapesrpg.util.coords.Coordinate
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.Tag
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.block.Action
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

        val b = e.clickedBlock!!
        val world = World.getWorld(b.world)
        var block = world.getBlock(Coordinate(b))
        if (block.blockData.onClick(e)) return

        if (Item.isGrapesItem(e.item)) {
            e.isCancelled = true
            val item = Item.fromItemStack(e.item!!)
            val attr = item.getAttribute(Block::class)

            if (attr != null && e.action == Action.RIGHT_CLICK_BLOCK && e.clickedBlock != null) {
                val bukkitBlock =
                    if (Tag.REPLACEABLE_PLANTS.isTagged(e.clickedBlock!!.type)) e.clickedBlock!!
                    else e.clickedBlock!!.getRelative(e.blockFace)

                block = world.getBlock(Coordinate(bukkitBlock))

                bukkitBlock.type = attr.type
                block.blockData = attr.blockData

                if (attr.reduce && e.player.gameMode != GameMode.CREATIVE) {
                    val i = e.item!!
                    if (i.amount > 1) i.amount--
                    else e.player.inventory.setItem(e.hand!!, ItemStack(Material.AIR))
                }
            }
        }
    }
}
