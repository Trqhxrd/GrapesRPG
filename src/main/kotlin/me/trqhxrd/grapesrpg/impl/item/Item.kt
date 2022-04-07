package me.trqhxrd.grapesrpg.impl.item

import de.tr7zw.changeme.nbtapi.NBTItem
import me.trqhxrd.grapesrpg.api.item.Attribute
import me.trqhxrd.grapesrpg.util.ModuleKey
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import java.util.concurrent.atomic.AtomicReference

open class Item(
    override val key: ModuleKey,
    override val type: Material,
    override val attributes: MutableSet<Attribute> = HashSet()
) : me.trqhxrd.grapesrpg.api.item.Item {

    override fun applyAttributes(item: ItemStack): ItemStack {
        val ref = AtomicReference(item)
        this.attributes.forEach { a -> ref.set(a.write(ref.get())) }
        return ref.get()
    }

    override fun build(): ItemStack {
        var item = ItemStack(this.type)

        val nbt = NBTItem(item)
        nbt.addCompound("grapes").setString("id", this.key.text)
        item = nbt.item

        item = this.applyAttributes(item)

        return item
    }
}
