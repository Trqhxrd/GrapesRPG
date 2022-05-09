package me.trqhxrd.grapesrpg.impl.world

import com.google.gson.Gson
import com.google.gson.JsonParser
import me.trqhxrd.grapesrpg.util.ModuleKey
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.player.PlayerInteractEvent
import me.trqhxrd.grapesrpg.api.world.BlockData as BlockDataAPI

abstract class BlockData<T : BlockData<T>>(override val id: ModuleKey) : BlockDataAPI<T> {

    companion object {
        val gson = Gson()
        val registry = mutableMapOf<ModuleKey, Class<out BlockData<*>>>()

        val KEY_EMPTY = ModuleKey("grapes", "void")

        init {
            registry[KEY_EMPTY] = Void::class.java
        }
    }

    override fun save() = gson.toJson(this)!!

    @Suppress("UNCHECKED_CAST")
    override fun load(serialized: String) {
        val key = gson.fromJson(
            JsonParser.parseString(serialized).asJsonObject.get("id").asJsonObject.toString(),
            ModuleKey::class.java
        )

        val clazz = registry[key]!!

        val d = gson.fromJson(serialized, clazz)
        if (this::class.java.isAssignableFrom(d::class.java)) this.apply(d as T)
    }

    override fun apply(data: T) {}

    class Void : BlockData<Void>(KEY_EMPTY) {

        override fun onClick(event: PlayerInteractEvent) {}
        override fun onBreak(event: BlockBreakEvent) {}
        override fun onPlace(event: BlockPlaceEvent) {}
    }
}
