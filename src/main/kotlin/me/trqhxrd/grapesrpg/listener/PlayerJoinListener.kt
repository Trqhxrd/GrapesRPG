package me.trqhxrd.grapesrpg.listener

import me.trqhxrd.grapesrpg.GrapesRPG
import me.trqhxrd.grapesrpg.game.item.DebugBlock
import me.trqhxrd.grapesrpg.util.AbstractListener
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerJoinEvent

class PlayerJoinListener() : AbstractListener(GrapesRPG.plugin) {

    @EventHandler
    fun onJoin(e: PlayerJoinEvent) {
        e.player.inventory.addItem(DebugBlock().build())
    }
}
