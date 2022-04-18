package me.trqhxrd.grapesrpg.listener

import me.trqhxrd.grapesrpg.game.item.weapons.swords.WoodenSword
import me.trqhxrd.grapesrpg.util.AbstractListener
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.plugin.Plugin

class PlayerJoinListener(plugin: Plugin) : AbstractListener(plugin) {

    @EventHandler
    fun onJoin(e: PlayerJoinEvent) {
        e.player.inventory.setItemInMainHand(WoodenSword().build())
    }
}
