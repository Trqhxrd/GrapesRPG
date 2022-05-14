package me.trqhxrd.grapesrpg.util.serialization

import com.google.gson.*
import me.trqhxrd.grapesrpg.GrapesRPG
import me.trqhxrd.grapesrpg.impl.world.blockdata.BlockData
import me.trqhxrd.grapesrpg.util.ModuleKey
import java.lang.reflect.Type

class BlockDataAdapter : JsonSerializer<BlockData<*>>, JsonDeserializer<BlockData<*>> {
    override fun serialize(data: BlockData<*>?, type: Type?, context: JsonSerializationContext?): JsonElement {
        println("Serialize: " + data.toString())
        if (data == null) throw NullPointerException("Can't parse null data!")
        val obj = JsonObject()
        obj.add("id", GrapesRPG.gson.toJsonTree(data.id))
        obj.add("data", data.serializeData())
        return obj
    }


    override fun deserialize(data: JsonElement?, type: Type?, context: JsonDeserializationContext?): BlockData<*> {
        println("Deserialize: " + data.toString())
        if (data == null) throw NullPointerException("Can't parse null data!")
        val key = GrapesRPG.gson.fromJson(data.asJsonObject["id"], ModuleKey::class.java)
        val klass = BlockData.registry[key]!!
        val obj = klass.getConstructor().newInstance()

        println(obj::class.qualifiedName)

        val dataJson: JsonElement = data.asJsonObject["data"] ?: return obj
        obj.deserializeData(dataJson)
        return obj
    }
}
