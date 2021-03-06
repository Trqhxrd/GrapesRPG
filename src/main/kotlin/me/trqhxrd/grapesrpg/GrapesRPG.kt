package me.trqhxrd.grapesrpg

import me.trqhxrd.grapesrpg.game.item.attribute.Damaging
import me.trqhxrd.grapesrpg.game.item.attribute.Durability
import me.trqhxrd.grapesrpg.game.item.attribute.Lore
import me.trqhxrd.grapesrpg.game.item.attribute.Name
import me.trqhxrd.grapesrpg.impl.item.attribute.AttributeRegistry
import me.trqhxrd.grapesrpg.listener.EntityDamageByEntityListener
import me.trqhxrd.grapesrpg.listener.PlayerJoinListener
import org.bukkit.plugin.Plugin
import java.util.logging.Logger

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

        lateinit var logger: Logger

        /**
         * This field contains an [AttributeRegistry].
         * Every [me.trqhxrd.grapesrpg.api.item.attribute.Attribute] has to be registered here.
         * If you want to create your own [me.trqhxrd.grapesrpg.api.item.attribute.Attribute],
         * have a look at some predefined attributes.
         */
        val attributes = AttributeRegistry()

        var debugMode: Boolean = false
            set(value) {
                if (field) return
                field = value
                this.enableDebugMode()
            }

        /**
         * This method needs to be called for the API to initialize.
         */
        fun init(plugin: Plugin) {
            this.plugin = plugin
            this.logger = this.plugin.logger

            this.plugin.config.options().copyDefaults(true)
            this.plugin.saveConfig()
            this.debugMode = this.plugin.config.getBoolean("debug")


            this.attributes.addAttribute(Damaging())
            this.attributes.addAttribute(Durability())
            this.attributes.addAttribute(Lore())
            this.attributes.addAttribute(Name())

            EntityDamageByEntityListener(this.plugin)
        }

        fun enableDebugMode() {
            this.logger.warning("Debug-mode enabled! Restart server to disable")
            PlayerJoinListener(this.plugin)
        }
    }
}
