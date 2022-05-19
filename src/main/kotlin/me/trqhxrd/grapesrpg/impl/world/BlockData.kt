package me.trqhxrd.grapesrpg.impl.world

import me.trqhxrd.grapesrpg.util.ModuleKey
import org.bukkit.Material
import me.trqhxrd.grapesrpg.api.world.BlockData as BlockDataAPI

abstract class BlockData<T : BlockData<T>>(
    override val id: ModuleKey,
    override val type: Material
) : BlockDataAPI<T> {

    companion object {
        val registry = mutableMapOf<ModuleKey, Class<out BlockData<*>>>()

        fun register(key: ModuleKey, clazz: Class<out BlockData<*>>) {
            registry[key] = clazz
        }
    }
}
