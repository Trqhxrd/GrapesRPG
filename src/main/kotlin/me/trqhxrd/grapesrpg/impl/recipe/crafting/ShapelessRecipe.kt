package me.trqhxrd.grapesrpg.impl.recipe.crafting

import me.trqhxrd.grapesrpg.api.item.Item
import me.trqhxrd.grapesrpg.api.recipe.Ingredient
import me.trqhxrd.grapesrpg.impl.recipe.ingredient.ItemIngredient
import me.trqhxrd.grapesrpg.impl.recipe.ingredient.ItemStackIngredient
import me.trqhxrd.grapesrpg.impl.recipe.ingredient.MaterialIngredient
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import me.trqhxrd.grapesrpg.api.recipe.crafting.ShapelessRecipe as ShapelessRecipeAPI

open class ShapelessRecipe(
    override val result: Item,
    override val ingredients: MutableList<Ingredient> = mutableListOf()
) : ShapelessRecipeAPI {

    override fun check(matrix: Array<ItemStack?>): Boolean {
        val ingredientsCopy = mutableSetOf(*ingredients.toTypedArray())
        val matrixCopy = mutableListOf(*matrix
            .filterNotNull()
            .filter { i -> i.type != Material.AIR }
            .toTypedArray())

        for (i in ingredients) {
            for (item in mutableListOf(*matrixCopy.toTypedArray())) {
                if (matrixCopy.isEmpty() || ingredientsCopy.isEmpty()) break
                if (i.check(item)) {
                    matrixCopy.remove(item)
                    ingredientsCopy.remove(i)
                    break
                }
            }
        }

        return ingredientsCopy.isEmpty() && matrixCopy.isEmpty()
    }

    override fun reduce(matrix: Array<ItemStack?>): Array<ItemStack?> {
        val m = matrix.clone()
        val used = mutableSetOf<Int>()

        for (i in this.ingredients) {
            for (index in m.indices) {
                val item = m[index] ?: ItemStack(Material.AIR)
                if (used.contains(index)) continue
                if (i.check(item)) {
                    m[index] = i.reduce(item)
                    used.add(index)
                }
            }
        }

        return m
    }

    override fun addIngredient(ingredient: Ingredient) {
        this.ingredients.add(ingredient)
    }

    override fun addIngredient(ingredient: Material) {
        this.ingredients.add(MaterialIngredient(ingredient))
    }

    override fun addIngredient(ingredient: ItemStack) {
        this.ingredients.add(ItemStackIngredient(ingredient))
    }

    override fun addIngredient(ingredient: Item) {
        this.ingredients.add(ItemIngredient(ingredient))
    }
}
