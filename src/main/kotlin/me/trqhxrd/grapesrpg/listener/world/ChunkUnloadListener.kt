package me.trqhxrd.grapesrpg.listener.world

import me.trqhxrd.grapesrpg.GrapesRPG
import me.trqhxrd.grapesrpg.impl.world.World
import me.trqhxrd.grapesrpg.util.AbstractListener
import me.trqhxrd.grapesrpg.util.coords.ChunkID
import org.bukkit.event.EventHandler
import org.bukkit.event.world.ChunkUnloadEvent

class ChunkUnloadListener : AbstractListener(GrapesRPG.plugin) {

    @EventHandler
    fun onChunkUnload(e: ChunkUnloadEvent) {
        World.getWorld(e).unloadChunk(ChunkID(e.chunk))
    }
}
