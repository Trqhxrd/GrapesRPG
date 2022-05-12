package me.trqhxrd.grapesrpg.listener.world

import me.trqhxrd.grapesrpg.GrapesRPG
import me.trqhxrd.grapesrpg.game.world.blockdata.CraftingTable
import me.trqhxrd.grapesrpg.impl.world.World
import me.trqhxrd.grapesrpg.util.AbstractListener
import me.trqhxrd.grapesrpg.util.coords.ChunkID
import me.trqhxrd.grapesrpg.util.coords.Coordinate
import org.bukkit.event.EventHandler
import org.bukkit.event.world.ChunkLoadEvent

class ChunkLoadListener : AbstractListener(GrapesRPG.plugin) {

    @EventHandler
    fun onChunkLoad(e: ChunkLoadEvent) {
        if (!World.worlds.map { it.bukkitWorld.name }.contains(e.world.name)) World(e.world, e.world.name)
        World.getWorld(e).loadChunk(ChunkID(e.chunk))
    }
}
