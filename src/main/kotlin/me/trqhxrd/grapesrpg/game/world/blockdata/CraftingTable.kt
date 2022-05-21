package me.trqhxrd.grapesrpg.game.world.blockdata

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import me.trqhxrd.grapesrpg.gui.crafting.CraftingGUI
import me.trqhxrd.grapesrpg.impl.world.BlockData
import me.trqhxrd.grapesrpg.util.ModuleKey
import org.bukkit.Material
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

class CraftingTable : BlockData<CraftingTable>(KEY, Material.CRAFTING_TABLE) {

    companion object {
        val KEY = ModuleKey("grapes", "crafting_table")
    }

    override fun onClick(e: PlayerInteractEvent): Boolean {
        if (e.action == Action.RIGHT_CLICK_BLOCK) {
            CraftingGUI(e.player)
            e.isCancelled = true
        }
        return true
    }

    override fun save() = JsonObject()

    override fun load(data: JsonElement) {}
}
