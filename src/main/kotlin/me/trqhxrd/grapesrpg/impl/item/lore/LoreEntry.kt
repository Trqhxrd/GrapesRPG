package me.trqhxrd.grapesrpg.impl.item.lore

import me.trqhxrd.grapesrpg.api.item.lore.LoreEntry

class LoreEntry(
    private vararg val lines: String,
    override val priority: Int = 0,
    private val emptyLinesBetweenEntries: Boolean = true
) : LoreEntry, Comparable<LoreEntry> {

    override fun append(lore: MutableList<String>) {
        if (this.emptyLinesBetweenEntries) lore.add("")
        lines.forEach { l -> lore.add(l) }
    }

    /**
     * Compares this object with the specified object for order. Returns zero if this object is equal
     * to the specified [other] object, a negative number if it's less than [other], or a positive number
     * if it's greater than [other].
     */
    override fun compareTo(other: LoreEntry): Int {
        return this.priority.compareTo(other.priority)
    }
}
