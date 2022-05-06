package me.trqhxrd.grapesrpg.util

/**
 * A [ModuleKey] is used by most stuff in the GrapesRPG.
 * It contains the name of a module and a custom name.
 *
 * @param module The name of a module.
 * @param key    The name of the key itself.
 */
data class ModuleKey(val module: String, val key: String) {
    /**
     * Static methods and fields.
     */
    companion object {
        /**
         * This method converts a [String] back into a [ModuleKey].
         * @param serialized The serialized [ModuleKey]
         * @return The deserialized [ModuleKey]
         */
        fun deserialize(serialized: String): ModuleKey {
            return this.deserializeOrNull(serialized)
                ?: throw IllegalArgumentException("\"$serialized\" is not a valid ModuleKey and therefore can't be deserialized.")
        }

        fun deserializeOrNull(serialized: String): ModuleKey? {
            val parts = serialized.split(":")
            return if (parts.size == 2) ModuleKey(parts[0], parts[1]) else null
        }
    }

    /**
     * This field contains the key serialized in the format module:key.
     */
    val serialized: String = "$module:$key"
}
