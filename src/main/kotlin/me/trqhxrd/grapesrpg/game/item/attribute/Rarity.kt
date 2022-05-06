package me.trqhxrd.grapesrpg.game.item.attribute

import de.tr7zw.changeme.nbtapi.NBTItem
import me.trqhxrd.grapesrpg.api.item.lore.LoreEntry
import me.trqhxrd.grapesrpg.impl.item.attribute.Attribute
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.ChatColor.*
import org.bukkit.inventory.ItemStack
import me.trqhxrd.grapesrpg.impl.item.lore.LoreEntry as LoreEntryImpl

class Rarity(var value: Value) : Attribute("grapes", "rarity") {

    constructor() : this(Value.COMMON)

    override fun read(item: ItemStack) {
        this.value = Value.valueOf(NBTItem(item).getCompound("grapes").getString("rarity"))
    }

    override fun write(item: ItemStack): ItemStack {
        val nbt = NBTItem(item)
        nbt.addCompound("grapes").setString("rarity", this.value.name)
        return nbt.item
    }

    override fun generateLoreEntry(): LoreEntry {
        var s = this.value.text

        for (c in this.value.color.reversed()) s = "$c$s"

        return LoreEntryImpl(s)
    }

    enum class Value(vararg val color: ChatColor, var text: String) {

        COMMON(WHITE, BOLD, text = "§f§lCOMMON"),
        UNCOMMON(GREEN, BOLD, text = "§a§lUNCOMMON"),
        RARE(AQUA, BOLD, text = "§b§lRARE"),
        EPIC(LIGHT_PURPLE, BOLD, text = "§d§lEPIC"),
        LEGENDARY(YELLOW, BOLD, text = "§e§lLEGENDARY"),
        MYTHIC(DARK_RED, BOLD, text = "§4§lMYTHIC"),
        HEAVENLY(BLUE, BOLD, text = "§9§lHEAVENLY"),
        GODLY(GOLD, BOLD, text = "§6§lGODLY"),
        DIVINE(DARK_PURPLE, BOLD, text = "§5§lDIVINE");
    }
}
