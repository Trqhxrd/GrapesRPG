package me.trqhxrd.grapesrpg.api.item

import org.bukkit.inventory.ItemStack

interface Attribute {

    fun read(item: ItemStack)

    fun write(item: ItemStack): ItemStack
}
