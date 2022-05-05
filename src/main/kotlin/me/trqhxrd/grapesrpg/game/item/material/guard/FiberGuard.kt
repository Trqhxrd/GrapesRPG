package me.trqhxrd.grapesrpg.game.item.material.guard

import me.trqhxrd.grapesrpg.game.item.attribute.Name
import me.trqhxrd.grapesrpg.game.item.material.PlantFiber
import me.trqhxrd.grapesrpg.game.item.material.handle.FiberHandle
import me.trqhxrd.grapesrpg.impl.item.Item
import me.trqhxrd.grapesrpg.impl.recipe.crafting.ShapedRecipe
import me.trqhxrd.grapesrpg.impl.recipe.ingredient.ItemIngredient
import me.trqhxrd.grapesrpg.impl.recipe.ingredient.MaterialIngredient
import org.bukkit.Material
import me.trqhxrd.grapesrpg.game.item.attribute.Material as MaterialAttr

class FiberGuard : Item(
    "fiber_guard",
    Material.STICK,
    mutableSetOf(
        Name("§aFiber Guard"),
        MaterialAttr()
    )
) {
    init {
        this.recipe = Recipe(this)
    }

    private class Recipe(item: Item) : ShapedRecipe(
        item,
        "___aba___",
        mutableMapOf(
            Pair('a', ItemIngredient(PlantFiber(), amount = 4)),
            Pair('b', MaterialIngredient(Material.STICK))
        )
    )
}
