package me.trqhxrd.grapesrpg.impl.world.blockdata

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import me.trqhxrd.grapesrpg.util.ModuleKey
import org.bukkit.Material
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.player.PlayerInteractEvent

class Void : BlockData<Void>(KEY, Material.AIR) {

    companion object {
        val KEY = ModuleKey("grapes", "void")
    }

    override fun onClick(event: PlayerInteractEvent) = false
    override fun onBreak(event: BlockBreakEvent) = false
    override fun onPlace(event: BlockPlaceEvent) = false

    override fun serializeData() = JsonObject()

    override fun deserializeData(data: JsonElement) {}
}
