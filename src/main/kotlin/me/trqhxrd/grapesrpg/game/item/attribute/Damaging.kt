package me.trqhxrd.grapesrpg.game.item.attribute

import de.tr7zw.changeme.nbtapi.NBTItem
import me.trqhxrd.grapesrpg.impl.item.attribute.Attribute
import me.trqhxrd.grapesrpg.impl.item.lore.LoreEntry
import org.bukkit.inventory.ItemStack

data class Damaging(var damage: Int) : Attribute("grapes", "damaging") {

    constructor() : this(0)

    override fun read(item: ItemStack) {
        val nbt = NBTItem(item)
        this.damage = nbt.getCompound("grapes").getInteger("damage")
    }

    override fun write(item: ItemStack): ItemStack {
        val nbt = NBTItem(item)
        nbt.addCompound("grapes").setInteger("damage", this.damage)

        return nbt.item
    }

    override fun generateLoreEntry(): me.trqhxrd.grapesrpg.api.item.lore.LoreEntry {
        return LoreEntry("§9Attack Damage: §c+$damage")
    }
}
