package me.trqhxrd.grapesrpg.util.serialization

import com.google.gson.*
import me.trqhxrd.grapesrpg.util.ModuleKey
import java.lang.reflect.Type

class ModuleKeyAdapter : JsonSerializer<ModuleKey>, JsonDeserializer<ModuleKey> {
    override fun serialize(data: ModuleKey?, type: Type?, context: JsonSerializationContext?): JsonElement {
        return if (data == null) JsonNull.INSTANCE
        else JsonPrimitive("${data.module}:${data.key}")
    }

    override fun deserialize(data: JsonElement?, type: Type?, context: JsonDeserializationContext?): ModuleKey {
        if (data == null) throw NullPointerException("Data can't be null!")

        val d = data.asString.split(":")
        if (d.size != 2) throw IllegalArgumentException("Could not parse key correctly! Data received: ${data.asString}")
        return ModuleKey(d[0], d[1])
    }
}
