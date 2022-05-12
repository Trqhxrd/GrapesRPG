package me.trqhxrd.grapesrpg.impl.world.blockdata

import me.trqhxrd.grapesrpg.util.ModuleKey
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.player.PlayerInteractEvent

class Void : BlockData<Void>(KEY) {

    companion object {
        val KEY = ModuleKey("grapes", "void")
    }

    override fun onClick(event: PlayerInteractEvent) {
        println(event.clickedBlock!!.location)
    }
    override fun onBreak(event: BlockBreakEvent) {}
    override fun onPlace(event: BlockPlaceEvent) {}
}
