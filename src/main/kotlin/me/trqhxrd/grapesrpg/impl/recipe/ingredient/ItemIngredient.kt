package me.trqhxrd.grapesrpg.impl.recipe.ingredient

import me.trqhxrd.grapesrpg.api.item.Item
import me.trqhxrd.grapesrpg.api.recipe.Ingredient
import me.trqhxrd.grapesrpg.util.ModuleKey
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import me.trqhxrd.grapesrpg.impl.item.Item as ItemImpl

class ItemIngredient(vararg val items: ModuleKey, val amount: Int = 1) : Ingredient {

    constructor(vararg items: Item) : this(*items.map { i -> i.key }.toTypedArray())

    init {
        if (items.isEmpty()) throw NullPointerException("A ${this::class.simpleName} requires at least one item!")
    }

    override fun check(item: ItemStack) =
        this.items.contains(ItemImpl.fromItemStack(item).key) && item.amount >= this.amount

    override fun reduce(item: ItemStack): ItemStack {
        return if (item.amount > amount) {
            item.amount -= amount
            item
        } else ItemStack(Material.AIR)
    }
}
