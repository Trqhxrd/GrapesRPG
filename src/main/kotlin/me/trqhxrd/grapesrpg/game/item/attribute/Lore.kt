package me.trqhxrd.grapesrpg.game.item.attribute

import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import de.tr7zw.changeme.nbtapi.NBTItem
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
        this.lines = Gson().fromJson(linesSerialized, object : TypeToken<Array<String>>() {}.type)
    }

    override fun write(item: ItemStack): ItemStack {
        val nbt = NBTItem(item)
        nbt.addCompound("grapes").setObject("lore", lines)
        return nbt.item
    }
}
