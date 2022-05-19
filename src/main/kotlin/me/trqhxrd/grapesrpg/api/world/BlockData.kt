package me.trqhxrd.grapesrpg.api.world

import com.google.gson.JsonElement
import me.trqhxrd.grapesrpg.util.ModuleKey
import org.bukkit.Material
import org.bukkit.event.player.PlayerInteractEvent

interface BlockData<T : BlockData<T>> {
    val id: ModuleKey
    val type: Material

    fun onClick(e: PlayerInteractEvent): Boolean

    fun save(): JsonElement
    fun load(data: JsonElement)
}
