package me.trqhxrd.grapesrpg.impl.world.blockdata

import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.google.gson.JsonParser
import me.trqhxrd.grapesrpg.util.ModuleKey
import org.bukkit.Material
import me.trqhxrd.grapesrpg.api.world.BlockData as BlockDataAPI

abstract class BlockData<T : BlockData<T>>(override val id: ModuleKey, override val type: Material) : BlockDataAPI<T> {

    companion object {
        val registry = mutableMapOf<ModuleKey, Class<out BlockData<*>>>()

        fun register(key: ModuleKey, clazz: Class<out BlockData<*>>) {
            this.registry[key] = clazz
        }
    }

    override fun save() = Gson().toJson(this)

    @Suppress("UNCHECKED_CAST")
    override fun load(serialized: String) {
        val key = Gson().fromJson(JsonParser.parseString(serialized).asJsonObject.get("id"), ModuleKey::class.java)

        val d: T = Gson().fromJson(serialized, object : TypeToken<T>(this::class.java) {}.type)
        if (this::class.java.isAssignableFrom(d::class.java)) this.apply(d)
    }

    override fun apply(data: T) {
        if (!this::class.java.isAssignableFrom(data::class.java))
            throw IllegalArgumentException(
                "Can't load data from a class isn't assignable from the loading class. " +
                        "(Required: ${this::class.qualifiedName}, Received: ${data::class.qualifiedName})"
            )
    }
}
