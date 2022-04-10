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
        GrapesRPG.init(this)

        this.logger.info("Hello World!")
    }
}
