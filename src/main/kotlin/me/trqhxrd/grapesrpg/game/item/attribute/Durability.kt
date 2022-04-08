package me.trqhxrd.grapesrpg.game.item.attribute

import de.tr7zw.changeme.nbtapi.NBTItem
import me.trqhxrd.grapesrpg.impl.item.attribute.Attribute
import org.bukkit.inventory.ItemStack

class Durability(var current: Int, var max: Int, var unbreakable: Boolean) :
    Attribute("grapes", "damaging") {

    constructor() : this(100)
    constructor(max: Int) : this(max, max)
    constructor(current: Int, max: Int) : this(current, max, false)

    override fun read(item: ItemStack) {
        val nbt = NBTItem(item)
        val compound = nbt.getCompound("grapes").getCompound("durability")
        this.current = compound.getInteger("current")
        this.max = compound.getInteger("max")
        this.unbreakable = compound.getBoolean("unbreakable")
    }

    override fun write(item: ItemStack): ItemStack {
        val nbt = NBTItem(item)
        val compound = nbt.addCompound("grapes").addCompound("durability")
        compound.setInteger("current", this.current)
        compound.setInteger("max", this.max)
        compound.setBoolean("unbreakable", this.unbreakable)
        return nbt.item
    }
}
