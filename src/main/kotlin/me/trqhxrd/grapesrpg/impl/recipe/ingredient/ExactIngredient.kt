package me.trqhxrd.grapesrpg.impl.recipe.ingredient

import me.trqhxrd.grapesrpg.api.recipe.Ingredient
import org.bukkit.inventory.ItemStack

class ExactIngredient(val item: ItemStack) : Ingredient {
    override fun accept(item: ItemStack) = item.isSimilar(this.item)
}
