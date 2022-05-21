package me.trqhxrd.grapesrpg.util

import org.bukkit.Bukkit
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.plugin.Plugin

/**
 * This is a base class for listeners with some default functionality.
 * @param plugin The plugin used to register this listener.
 * @param register Whether you want the listener to be registered on object creation. (default: true)
 * @author Trqhxrd
 * @since 1.0
 */
abstract class AbstractListener(val plugin: Plugin, register: Boolean = true) : Listener {
    /**
     * static
     */
    companion object {
        /**
         * This field contains all listeners and their current state of registration.
         */
        val listeners = mutableMapOf<AbstractListener, Boolean>()

        /**
         * This method unregisters all listeners.
         */
        fun unregisterAll() = listeners.keys.stream()
            .filter { listeners[it]==true }
            .forEach { it.unregister() }
    }

    /**
     * This method registers the listener if the "register" parameter is set to true.
     */
    init {
        if (register) this.register()
    }

    /**
     * This method registers the listener.
     */
    fun register() {
        listeners[this] = true
        Bukkit.getPluginManager().registerEvents(this, plugin)
    }

    /**
     * This method unregisters the listener.
     */
    fun unregister() {
        listeners[this] = false
        HandlerList.unregisterAll(this)
    }
}
