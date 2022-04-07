package me.trqhxrd.grapesrpg.util

data class ModuleKey(val module: String, val key: String) {

    val text: String = "$module:$key"
}
