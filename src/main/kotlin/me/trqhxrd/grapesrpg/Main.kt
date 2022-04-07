package me.trqhxrd.grapesrpg

import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {

    override fun onEnable() {
        Bukkit.getConsoleSender().sendMessage("Hello World!")
    }
}
