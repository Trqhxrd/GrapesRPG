package me.trqhxrd.grapesrpg.impl.recipe.crafting

import me.trqhxrd.grapesrpg.api.item.Item
import me.trqhxrd.grapesrpg.api.recipe.Ingredient
import me.trqhxrd.grapesrpg.api.recipe.crafting.ShapedRecipe
import me.trqhxrd.grapesrpg.impl.recipe.ingredient.ItemIngredient
import me.trqhxrd.grapesrpg.impl.recipe.ingredient.ItemStackIngredient
import me.trqhxrd.grapesrpg.impl.recipe.ingredient.MaterialIngredient
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

open class ShapedRecipe(
    override val result: Item,
    override val shape: String,
    override val ingredients: MutableMap<Char, Ingredient>
) : ShapedRecipe {

    override fun check(matrix: Array<ItemStack?>): Boolean {
        val status = BooleanArray(matrix.size)
        for ((index, item) in matrix.withIndex()) {
            val ingredientKey = this.shape[index]
            val ingredient = this.ingredients[ingredientKey]
            if (item == null && ingredient == null) status[index] = true
            else if (item == null || ingredient == null) status[index] = false
            else status[index] = ingredient.check(item)
        }

        return !status.any { b -> !b }
    }

    override fun reduce(matrix: Array<ItemStack?>): Array<ItemStack?> {
        for ((index, item) in matrix.withIndex()) {
            if (item == null) continue
            val char = this.shape[index]
            val ingredient = this.ingredients[char] ?: continue
            matrix[index] = ingredient.reduce(item)
        }
        return matrix
    }

    override fun addIngredient(key: Char, ingredient: Ingredient) {
        this.ingredients[key] = ingredient
    }

    override fun addIngredient(key: Char, ingredient: Material) =
        this.addIngredient(key, MaterialIngredient(ingredient))


    override fun addIngredient(key: Char, ingredient: ItemStack) =
        this.addIngredient(key, ItemStackIngredient(ingredient))


    override fun addIngredient(key: Char, ingredient: Item) =
        this.addIngredient(key, ItemIngredient(ingredient))
}
