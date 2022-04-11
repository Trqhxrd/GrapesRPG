package me.trqhxrd.grapesrpg.api.recipe

import org.bukkit.inventory.ItemStack

interface Ingredient {
    fun accept(item: ItemStack): Boolean
}
