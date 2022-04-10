package me.trqhxrd.grapesrpg.api.item.lore

interface LoreEntry {
    val priority: Int
    fun append(lore: MutableList<String>)
}
