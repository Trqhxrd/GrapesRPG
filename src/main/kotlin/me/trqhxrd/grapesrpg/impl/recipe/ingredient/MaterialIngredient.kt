package me.trqhxrd.grapesrpg.impl.recipe.ingredient

import me.trqhxrd.grapesrpg.api.recipe.Ingredient
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class MaterialIngredient(vararg val materials: Material, val amount: Int = 1) : Ingredient {
    init {
        if (materials.isEmpty()) throw NullPointerException("A ${this::class.simpleName} requires at least one material!")
    }

    override fun check(item: ItemStack): Boolean = this.materials.contains(item.type) && item.amount >= this.amount

    override fun reduce(item: ItemStack): ItemStack {
        return if (item.amount > amount) {
            item.amount -= amount
            item
        } else ItemStack(Material.AIR)
    }
}
