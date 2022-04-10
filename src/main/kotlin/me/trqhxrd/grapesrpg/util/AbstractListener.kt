package me.trqhxrd.grapesrpg.util

import org.bukkit.Bukkit
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.plugin.Plugin

abstract class AbstractListener(val plugin: Plugin, registered: Boolean = true) : Listener {

    var registered: Boolean = registered
        private set

    companion object {
        val listeners = mutableListOf<AbstractListener>()

        fun deregisterAll() = listeners.forEach { l -> l.deregister() }
    }

    init {
        if (this.registered) this.register()
    }

    fun register() {
        this.registered = true
        Bukkit.getPluginManager().registerEvents(this, plugin)
    }

    fun deregister() {
        this.registered = false
        HandlerList.unregisterAll(this)
    }
}
