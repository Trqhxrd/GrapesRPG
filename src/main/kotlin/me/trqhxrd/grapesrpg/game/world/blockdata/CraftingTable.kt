package me.trqhxrd.grapesrpg.game.world.blockdata

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import me.trqhxrd.grapesrpg.gui.crafting.CraftingGUI
import me.trqhxrd.grapesrpg.impl.world.blockdata.BlockData
import me.trqhxrd.grapesrpg.util.ModuleKey
import org.bukkit.Material
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.player.PlayerInteractEvent

class CraftingTable : BlockData<CraftingTable>(KEY, Material.CRAFTING_TABLE) {

    companion object {
        val KEY = ModuleKey("grapes", "crafting_table")
    }

    override fun onClick(event: PlayerInteractEvent): Boolean {
        return if (event.action == Action.RIGHT_CLICK_BLOCK) {
            CraftingGUI(event.player)
            true
        } else false
    }

    override fun onBreak(event: BlockBreakEvent) = false

    override fun serializeData() = JsonObject()

    override fun deserializeData(data: JsonElement) {}
}
