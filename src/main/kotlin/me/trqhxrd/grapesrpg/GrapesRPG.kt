package me.trqhxrd.grapesrpg

import me.trqhxrd.grapesrpg.game.item.attribute.Damaging
import me.trqhxrd.grapesrpg.game.item.attribute.Durability
import me.trqhxrd.grapesrpg.game.item.attribute.Lore
import me.trqhxrd.grapesrpg.game.item.attribute.Name
import me.trqhxrd.grapesrpg.impl.item.attribute.AttributeRegistry
import me.trqhxrd.grapesrpg.listener.EntityDamageByEntityListener
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin

/**
 * This class contains some utility methods used by the plugin.
 * @author Trqhxrd
 * @since 1.0
 */
class GrapesRPG private constructor() {

    companion object {
        /**
         * This field contains the [Plugin], that owns the API.
         * In most of the cases that's an instance of [Main].
         */
        lateinit var plugin: Plugin

        /**
         * This field contains an [AttributeRegistry].
         * Every [me.trqhxrd.grapesrpg.api.item.attribute.Attribute] has to be registered here.
         * If you want to create your own [me.trqhxrd.grapesrpg.api.item.attribute.Attribute],
         * have a look at some predefined attributes.
         */
        val attributes = AttributeRegistry()

        /**
         * This method needs to be called for the API to initialize.
         */
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
