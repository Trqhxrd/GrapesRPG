package me.trqhxrd.grapesrpg.impl.world.blockdata

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import me.trqhxrd.grapesrpg.impl.world.BlockData
import me.trqhxrd.grapesrpg.util.ModuleKey
import org.bukkit.Material
import org.bukkit.event.player.PlayerInteractEvent

class Void : BlockData<Void>(KEY, Material.AIR) {

    companion object {
        val KEY = ModuleKey("grapes", "void")
    }

    override fun onClick(e: PlayerInteractEvent) = false

    override fun save() = JsonObject()
    override fun load(data: JsonElement) {}
}
