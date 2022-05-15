package me.trqhxrd.grapesrpg.api.world

import com.google.gson.JsonElement
import me.trqhxrd.grapesrpg.util.ModuleKey
import org.bukkit.Material
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.player.PlayerInteractEvent

interface BlockData<T : BlockData<T>> {
    val id: ModuleKey
    val type: Material

    fun onClick(event: PlayerInteractEvent): Boolean
    fun onBreak(event: BlockBreakEvent): Boolean

    fun serializeData(): JsonElement
    fun deserializeData(data: JsonElement)
}
