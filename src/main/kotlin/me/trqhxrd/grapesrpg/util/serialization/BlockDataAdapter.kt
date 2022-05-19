package me.trqhxrd.grapesrpg.util.serialization

import com.google.gson.*
import me.trqhxrd.grapesrpg.GrapesRPG
import me.trqhxrd.grapesrpg.impl.world.BlockData
import me.trqhxrd.grapesrpg.util.ModuleKey
import java.lang.reflect.Type

class BlockDataAdapter : JsonSerializer<BlockData<*>>, JsonDeserializer<BlockData<*>> {
    override fun serialize(data: BlockData<*>?, type: Type?, context: JsonSerializationContext?): JsonElement {
        if (data == null) throw NullPointerException("Can't parse null data!")
        val obj = JsonObject()
        obj.add("id", GrapesRPG.gson.toJsonTree(data.id))
        obj.add("data", data.save())
        return obj
    }


    override fun deserialize(data: JsonElement?, type: Type?, context: JsonDeserializationContext?): BlockData<*> {
        if (data == null) throw NullPointerException("Can't parse null data!")
        val id = GrapesRPG.gson.fromJson(data.asJsonObject["id"], ModuleKey::class.java)
        val klass = BlockData.registry[id]!!
        val obj = klass.getConstructor().newInstance()

        val dataJson: JsonElement = data.asJsonObject["data"] ?: return obj
        obj.load(dataJson)
        return obj
    }
}
