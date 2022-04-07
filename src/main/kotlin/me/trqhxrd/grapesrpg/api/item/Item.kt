package me.trqhxrd.grapesrpg.api.item

import me.trqhxrd.grapesrpg.util.ModuleKey
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

interface Item {

    val key: ModuleKey

    val type: Material

    val attributes: Set<Attribute>

    fun applyAttributes(item: ItemStack): ItemStack

    fun build(): ItemStack
}
