package me.trqhxrd.grapesrpg

import me.trqhxrd.grapesrpg.game.item.attribute.*
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
    /**
     * Static methods and fields.
     */
    companion object {
        /**
         * This field contains the [Plugin], that owns the API.
         * In most of the cases that's an instance of [Main].
         */
        lateinit var plugin: Plugin

        /**
         * This field contains the [Logger] of the plugin passed into [GrapesRPG.Companion.init]
         */
        lateinit var logger: Logger

        /**
         * This field contains an [AttributeRegistry].
         * Every [me.trqhxrd.grapesrpg.api.item.attribute.Attribute] has to be registered here.
         * If you want to create your own [me.trqhxrd.grapesrpg.api.item.attribute.Attribute],
         * have a look at some predefined attributes.
         */
        val attributes = AttributeRegistry()

        /**
         * Whether debug mode is enabled. When debug mode is enabled the plugin does stuff like giving out items on join.
         */
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
            this.attributes.addAttribute(Todo())
            this.attributes.addAttribute(Material())

            EntityDamageByEntityListener(this.plugin)
        }

        /**
         * This method contains all things, that need to be executed when enabling debug mode.
         */
        fun enableDebugMode() {
            this.logger.warning("Debug-mode enabled! Restart server to disable")
            PlayerJoinListener(this.plugin)
        }
    }
}
