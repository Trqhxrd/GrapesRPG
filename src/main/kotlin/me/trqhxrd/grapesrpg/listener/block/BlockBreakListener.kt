package me.trqhxrd.grapesrpg.listener.block

import me.trqhxrd.grapesrpg.GrapesRPG
import me.trqhxrd.grapesrpg.impl.world.World
import me.trqhxrd.grapesrpg.util.AbstractListener
import org.bukkit.event.EventHandler
import org.bukkit.event.block.BlockBreakEvent

class BlockBreakListener : AbstractListener(GrapesRPG.plugin) {

    @EventHandler
    fun onBlockBreak(e: BlockBreakEvent) {
        World.getWorld(e.block.world).getBlock(e.block.location).blockData.onBreak(e)
    }
}
