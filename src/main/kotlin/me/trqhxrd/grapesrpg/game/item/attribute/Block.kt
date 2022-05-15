package me.trqhxrd.grapesrpg.game.item.attribute

import com.google.common.reflect.TypeToken
import de.tr7zw.changeme.nbtapi.NBTItem
import me.trqhxrd.grapesrpg.GrapesRPG
import me.trqhxrd.grapesrpg.api.world.BlockData
import me.trqhxrd.grapesrpg.impl.item.attribute.Attribute
import me.trqhxrd.grapesrpg.impl.world.blockdata.Void
import org.bukkit.inventory.ItemStack
import me.trqhxrd.grapesrpg.impl.world.blockdata.BlockData as BlockDataImpl

class Block(var blockData: BlockData<*>) : Attribute("grapes", "block") {

    constructor() : this(Void())

    override fun read(item: ItemStack) {
        val nbt = NBTItem(item)
        val d = nbt.getCompound("grapes").getString("blockData")
        this.blockData = GrapesRPG.gson.fromJson(d, object : TypeToken<BlockDataImpl<*>>() {}.type)
    }

    override fun write(item: ItemStack): ItemStack {
        val nbt = NBTItem(item)
        nbt.getCompound("grapes").setString("blockData", GrapesRPG.gson.toJson(this.blockData))
        return nbt.item
    }
}
