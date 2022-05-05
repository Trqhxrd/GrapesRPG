package me.trqhxrd.grapesrpg.game.item.material.fiber

import me.trqhxrd.grapesrpg.game.item.attribute.Name
import me.trqhxrd.grapesrpg.impl.item.Item
import me.trqhxrd.grapesrpg.impl.recipe.crafting.ShapedRecipe
import me.trqhxrd.grapesrpg.impl.recipe.ingredient.ItemIngredient
import me.trqhxrd.grapesrpg.impl.recipe.ingredient.MaterialIngredient
import org.bukkit.Material
import me.trqhxrd.grapesrpg.game.item.attribute.Material as MaterialAttr

class FiberHandle : Item(
    "fiber_handle",
    Material.STICK,
    mutableSetOf(
        Name("§aFiber Handle"),
        MaterialAttr()
    )
) {
    init {
        this.recipe = Recipe(this)
    }

    private class Recipe(item: Item) : ShapedRecipe(
        item,
        "_a_aba_a_",
        mutableMapOf(
            Pair('a', ItemIngredient(PlantFiber(), amount = 4)),
            Pair('b', MaterialIngredient(Material.STICK))
        )
    )
}

