package me.trqhxrd.grapesrpg.api.recipe

import org.bukkit.inventory.ItemStack

interface Ingredient {
    fun check(item: ItemStack): Boolean
    fun reduce(item: ItemStack): ItemStack
}
