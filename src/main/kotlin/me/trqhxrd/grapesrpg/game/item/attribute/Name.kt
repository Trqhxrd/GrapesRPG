package me.trqhxrd.grapesrpg.game.item.attribute

import de.tr7zw.changeme.nbtapi.NBTItem
import me.trqhxrd.grapesrpg.impl.item.attribute.Attribute
import org.bukkit.inventory.ItemStack

class Name(var name: String) : Attribute("grapes", "name") {

    constructor() : this("default name")

    override fun read(item: ItemStack) {
        this.name = NBTItem(item).addCompound("grapes").getString("name")
    }

    override fun write(item: ItemStack): ItemStack {
        val nbt = NBTItem(item)
        nbt.addCompound("grapes").setString("name", this.name)
        return nbt.item
    }

    override fun apply(item: ItemStack): ItemStack {
        val meta = item.itemMeta
        meta!!.setDisplayName(this.name)
        item.itemMeta = meta
        return item
    }
}
