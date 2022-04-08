package me.trqhxrd.grapesrpg.api.item.attribute

import me.trqhxrd.grapesrpg.util.ModuleKey
import org.bukkit.inventory.ItemStack

interface Attribute {

    val moduleKey: ModuleKey

    fun read(item: ItemStack)

    fun write(item: ItemStack): ItemStack
}
