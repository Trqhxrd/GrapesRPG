package me.trqhxrd.grapesrpg.listener.world

import kotlinx.coroutines.launch
import me.trqhxrd.grapesrpg.GrapesRPG
import me.trqhxrd.grapesrpg.impl.world.World
import me.trqhxrd.grapesrpg.impl.world.WorldScope
import me.trqhxrd.grapesrpg.util.AbstractListener
import me.trqhxrd.grapesrpg.util.coords.ChunkID
import org.bukkit.event.EventHandler
import org.bukkit.event.world.ChunkUnloadEvent

class ChunkUnloadListener : AbstractListener(GrapesRPG.plugin) {

    @EventHandler
    fun onChunkUnload(e: ChunkUnloadEvent) {
        WorldScope.launch {
            World.getWorld(e).unloadChunkAsync(ChunkID(e.chunk))
        }
    }
}
