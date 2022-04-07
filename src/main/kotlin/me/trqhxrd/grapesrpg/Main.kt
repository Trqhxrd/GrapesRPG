package me.trqhxrd.grapesrpg

import me.trqhxrd.grapesrpg.listener.PlayerJoinListener
import org.bukkit.Bukkit
import org.bukkit.plugin.PluginDescriptionFile
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.plugin.java.JavaPluginLoader
import java.io.File

class Main : JavaPlugin {

    companion object {
        var debugMode: Boolean = false
    }

    constructor() : super()

    constructor(loader: JavaPluginLoader, description: PluginDescriptionFile, dataFolder: File, file: File) : super(
        loader,
        description,
        dataFolder,
        file
    )

    override fun onEnable() {
        this.config.options().copyDefaults(true)
        this.saveConfig()

        debugMode = this.config.getBoolean("debug", false)
        if (debugMode) this.logger.info("Debug-Mode enabled.")
        if (debugMode) Bukkit.getPluginManager().registerEvents(PlayerJoinListener(), this)

        this.logger.warning("Hello World!")
    }
}
