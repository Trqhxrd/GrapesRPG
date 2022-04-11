package me.trqhxrd.grapesrpg.impl.recipe.ingredient

import me.trqhxrd.grapesrpg.api.recipe.Ingredient
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class MaterialIngredient(val mat: Material) : Ingredient {
    override fun accept(item: ItemStack) = mat == item.type
}
