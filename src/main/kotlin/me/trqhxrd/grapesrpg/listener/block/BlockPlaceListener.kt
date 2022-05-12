package me.trqhxrd.grapesrpg.listener.block

import me.trqhxrd.grapesrpg.GrapesRPG
import me.trqhxrd.grapesrpg.impl.world.World
import me.trqhxrd.grapesrpg.util.AbstractListener
import org.bukkit.event.EventHandler
import org.bukkit.event.block.BlockPlaceEvent

class BlockPlaceListener : AbstractListener(GrapesRPG.plugin) {

    @EventHandler
    fun onBlockPlace(e: BlockPlaceEvent) {
        println(3)
        World.getWorld(e.block.world).getBlock(e.block.location).data.onPlace(e)
    }
}
