package me.trqhxrd.grapesrpg.listener

import me.trqhxrd.grapesrpg.game.item.weapons.swords.WoodenSword
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class PlayerJoinListener : Listener {

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        event.player.inventory.setItemInMainHand(WoodenSword().build())
    }
}
