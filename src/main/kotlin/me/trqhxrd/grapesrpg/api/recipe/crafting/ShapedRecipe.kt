package me.trqhxrd.grapesrpg.api.recipe.crafting

import me.trqhxrd.grapesrpg.api.item.Item
import me.trqhxrd.grapesrpg.api.recipe.Ingredient
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

interface ShapedRecipe : CraftingRecipe {
    val shape: String
    val ingredients: MutableMap<Char, Ingredient>
    fun addIngredient(key: Char, ingredient: Ingredient)
    fun addIngredient(key: Char, ingredient: Material)
    fun addIngredient(key: Char, ingredient: ItemStack)
    fun addIngredient(key: Char, ingredient: Item)
}
