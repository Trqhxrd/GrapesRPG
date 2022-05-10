package me.trqhxrd.grapesrpg.impl.world

import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonObject
import me.trqhxrd.grapesrpg.util.ModuleKey
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.player.PlayerInteractEvent
import me.trqhxrd.grapesrpg.api.world.BlockData as BlockDataAPI

@Serializable
@SerialName("BlockData")
abstract class BlockData<T : BlockData<T>>(override val id: ModuleKey) : BlockDataAPI<T> {

    companion object {
        val registry = mutableMapOf<ModuleKey, Pair<Class<out BlockData<*>>, KSerializer<out BlockData<*>>>>()

        val KEY_EMPTY = ModuleKey("grapes", "void")

        init {
            registry[KEY_EMPTY] = Void::class.java to Void.serializer()
        }
    }

    override fun save() = Json.encodeToString(this)

    @Suppress("UNCHECKED_CAST")
    override fun load(serialized: String) {
        val key = Json.decodeFromJsonElement<ModuleKey>(
            Json.decodeFromString<JsonObject>(serialized)["id"]!!.jsonObject
        )

        val clazz = registry[key]!!.first
        val serializer = registry[key]!!.second

        val d = serializer.deserialize(Json.decodeFromString(serialized))
        if (this::class.java.isAssignableFrom(d::class.java)) this.apply(d as T)
    }

    override fun apply(data: T) {}

    @Serializable
    class Void : BlockData<Void>(KEY_EMPTY) {

        override fun onClick(event: PlayerInteractEvent) {}
        override fun onBreak(event: BlockBreakEvent) {}
        override fun onPlace(event: BlockPlaceEvent) {}
    }
}
