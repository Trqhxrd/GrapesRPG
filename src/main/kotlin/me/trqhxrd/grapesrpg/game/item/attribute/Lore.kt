package me.trqhxrd.grapesrpg.game.item.attribute

import de.tr7zw.changeme.nbtapi.NBTItem
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import me.trqhxrd.grapesrpg.impl.item.attribute.Attribute
import me.trqhxrd.grapesrpg.impl.item.lore.LoreEntry
import org.bukkit.inventory.ItemStack

class Lore(vararg var lines: String = arrayOf()) : Attribute("grapes", "lore") {

    override fun generateLoreEntry(): LoreEntry {
        return LoreEntry(*lines)
    }

    override fun read(item: ItemStack) {
        val nbt = NBTItem(item)
        val linesSerialized = nbt.getCompound("grapes").getString("lore")
        this.lines = Json.decodeFromString(linesSerialized)
    }

    override fun write(item: ItemStack): ItemStack {
        val nbt = NBTItem(item)
        nbt.addCompound("grapes").setObject("lore", lines)
        return nbt.item
    }
}
