package me.trqhxrd.grapesrpg.game.item.attribute

import com.google.common.reflect.TypeToken
import de.tr7zw.changeme.nbtapi.NBTItem
import me.trqhxrd.grapesrpg.GrapesRPG
import me.trqhxrd.grapesrpg.api.world.BlockData
import me.trqhxrd.grapesrpg.impl.item.attribute.Attribute
import me.trqhxrd.grapesrpg.impl.world.blockdata.Void
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import me.trqhxrd.grapesrpg.impl.world.BlockData as BlockDataImpl

class Block(
    var blockData: BlockData<*>,
    var type: Material,
    var reduce: Boolean = true
) : Attribute("grapes", "block") {

    constructor() : this(Void(), Material.AIR)

    override fun read(item: ItemStack) {
        val nbt = NBTItem(item)
        val compound = nbt.getCompound("grapes").getCompound("block")
        this.reduce = compound.getBoolean("reduce")
        this.type = Material.valueOf(compound.getString("type"))
        val d = compound.getString("blockData")
        this.blockData = GrapesRPG.gson.fromJson(d, object : TypeToken<BlockDataImpl<*>>() {}.type)
    }

    override fun write(item: ItemStack): ItemStack {
        val nbt = NBTItem(item)
        val compound = nbt.addCompound("grapes").addCompound("block")!!
        compound.setString("blockData", GrapesRPG.gson.toJson(this.blockData))
        compound.setString("type", this.type.name)
        compound.setBoolean("reduce", this.reduce)
        return nbt.item
    }
}
