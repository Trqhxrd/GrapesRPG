package me.trqhxrd.grapesrpg

import me.trqhxrd.grapesrpg.game.item.attribute.Damaging
import me.trqhxrd.grapesrpg.game.item.attribute.Durability
import me.trqhxrd.grapesrpg.game.item.attribute.Lore
import me.trqhxrd.grapesrpg.game.item.attribute.Name
import me.trqhxrd.grapesrpg.impl.item.attribute.AttributeRegistry
import me.trqhxrd.grapesrpg.listener.EntityDamageByEntityListener
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin

class GrapesRPG {
    companion object {
        private lateinit var plugin: Plugin
        val attributes = AttributeRegistry()

        fun init(plugin: Plugin) {
            GrapesRPG.plugin = plugin

            this.attributes.addAttribute(Damaging())
            this.attributes.addAttribute(Durability())
            this.attributes.addAttribute(Lore())
            this.attributes.addAttribute(Name())

            Bukkit.getPluginManager().registerEvents(EntityDamageByEntityListener(), plugin)
        }
    }
}
