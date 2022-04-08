package me.trqhxrd.grapesrpg.util

data class ModuleKey(val module: String, val key: String) {

    companion object {
        fun deserialize(serialized: String): ModuleKey {
            val parts = serialized.split(":")
            return ModuleKey(parts[0], parts[1])
        }
    }

    val serialized: String = "$module:$key"
}
