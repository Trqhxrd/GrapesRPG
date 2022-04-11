package me.trqhxrd.grapesrpg.impl.recipe.ingredient

import me.trqhxrd.grapesrpg.api.item.Item
import me.trqhxrd.grapesrpg.api.recipe.Ingredient
import me.trqhxrd.grapesrpg.util.ModuleKey
import org.bukkit.inventory.ItemStack
import me.trqhxrd.grapesrpg.impl.item.Item as ItemImpl

class TypeIngredient(val moduleKey: ModuleKey) : Ingredient {

    constructor(item: Item) : this(item.key)

    override fun accept(item: ItemStack) = ItemImpl.fromItemStack(item).key == moduleKey
}
