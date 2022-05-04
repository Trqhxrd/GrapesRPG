package me.trqhxrd.grapesrpg.impl.recipe.ingredient

import me.trqhxrd.grapesrpg.api.recipe.Ingredient
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class ItemStackIngredient(vararg val items: ItemStack,val amount:Int = 1) : Ingredient {
    init {
        if (items.isEmpty()) throw NullPointerException("A ${this::class.simpleName} requires at least one item!")
    }

    override fun check(item: ItemStack) = this.items.any { i -> i.isSimilar(item) }&& item.amount >= this.amount


    override fun reduce(item: ItemStack): ItemStack {
        return if (item.amount > amount) {
            item.amount -= amount
            item
        } else ItemStack(Material.AIR)
    }
}
