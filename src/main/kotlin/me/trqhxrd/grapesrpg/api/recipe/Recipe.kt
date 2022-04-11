package me.trqhxrd.grapesrpg.api.recipe

import me.trqhxrd.grapesrpg.api.item.Item
import org.bukkit.inventory.ItemStack

interface Recipe {
    val result: Item
    val bindings: MutableMap<Ingredient, Int>
    fun check(matrix: Array<ItemStack>, bindings: Array<ItemStack>): Boolean
    fun run(menu: CraftingMenu)
}
