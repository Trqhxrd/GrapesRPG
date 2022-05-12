package me.trqhxrd.grapesrpg.game.item.attribute

import com.google.gson.Gson
import com.google.gson.JsonObject
import de.tr7zw.changeme.nbtapi.NBTItem
import me.trqhxrd.grapesrpg.api.world.BlockData
import me.trqhxrd.grapesrpg.impl.item.attribute.Attribute
import me.trqhxrd.grapesrpg.impl.world.blockdata.Void
import me.trqhxrd.grapesrpg.util.ModuleKey
import org.bukkit.inventory.ItemStack
import me.trqhxrd.grapesrpg.impl.world.blockdata.BlockData as BlockDataImpl

class Block(var data: BlockData<*>) : Attribute("grapes", "block") {

    constructor() : this(Void())

    override fun read(item: ItemStack) {
        val nbt = NBTItem(item)
        val d = nbt.getCompound("grapes").getString("blockData")
        val json = Gson().fromJson(d, JsonObject::class.java)
        val key = Gson().fromJson(json.get("id").asJsonObject, ModuleKey::class.java)
        val klass = BlockDataImpl.registry[key]!!

        this.data = klass.getConstructor().newInstance()
        this.data.load(d)
    }

    override fun write(item: ItemStack): ItemStack {
        val nbt = NBTItem(item)
        nbt.getCompound("grapes").setString("blockData", this.data.save())
        return nbt.item
    }
}
