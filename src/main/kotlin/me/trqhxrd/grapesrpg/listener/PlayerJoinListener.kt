package me.trqhxrd.grapesrpg.listener

import me.trqhxrd.grapesrpg.game.item.weapons.swords.*
import me.trqhxrd.grapesrpg.util.AbstractListener
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.plugin.Plugin

class PlayerJoinListener(plugin: Plugin) : AbstractListener(plugin) {

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        event.player.inventory.setItem(0, WoodenSword().build())
        event.player.inventory.setItem(1, StoneSword().build())
        event.player.inventory.setItem(2, IronSword().build())
        event.player.inventory.setItem(3, GoldSword().build())
        event.player.inventory.setItem(4, SteelSword().build())
        event.player.inventory.setItem(5, DiamondSword().build())
        event.player.inventory.setItem(5, NetheriteSword().build())
    }
}
