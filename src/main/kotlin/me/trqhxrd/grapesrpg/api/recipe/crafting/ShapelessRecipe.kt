package me.trqhxrd.grapesrpg.api.recipe.crafting

import me.trqhxrd.grapesrpg.api.item.Item
import me.trqhxrd.grapesrpg.api.recipe.Ingredient
import me.trqhxrd.grapesrpg.api.recipe.Recipe
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

interface ShapelessRecipe : CraftingRecipe {
    val ingredients: MutableSet<Ingredient>
    fun addIngredient(ingredient: Ingredient)
    fun addIngredient(ingredient: Material)
    fun addIngredient(ingredient: ItemStack)
    fun addIngredient(ingredient: Item)
}
