package me.trqhxrd.grapesrpg.game.item.attribute

import de.tr7zw.changeme.nbtapi.NBTItem
import me.trqhxrd.grapesrpg.impl.item.Item
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
        var finalName = this.name

        if (Item.isGrapesItem(item)) {
            val i = Item.fromItemStack(item)
            val rarity = i.getAttribute(Rarity::class)
            if (rarity != null)
                // On purpose not reversed :D
                for (c in rarity.value.color)
                    finalName = "${c}$finalName"
        }

        meta!!.setDisplayName(finalName)
        item.itemMeta = meta
        return item
    }
}
