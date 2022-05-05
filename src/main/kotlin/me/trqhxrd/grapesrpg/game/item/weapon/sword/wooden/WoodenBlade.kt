package me.trqhxrd.grapesrpg.game.item.weapon.sword.wooden

import me.trqhxrd.grapesrpg.game.item.attribute.Name
import me.trqhxrd.grapesrpg.impl.item.Item
import me.trqhxrd.grapesrpg.impl.recipe.crafting.ShapedRecipe
import me.trqhxrd.grapesrpg.impl.recipe.ingredient.MaterialIngredient
import org.bukkit.Material
import org.bukkit.Tag
import me.trqhxrd.grapesrpg.game.item.attribute.Material as MaterialAttr

class WoodenBlade : Item(
    "wooden_blade",
    Material.STICK,
    mutableSetOf(
        Name("§aWooden Blade"),
        MaterialAttr()
    )
) {
    init {
        this.recipe = Recipe(this)
    }

    private class Recipe(item: WoodenBlade) : ShapedRecipe(
        item,
        "__a_a__a_",
        mutableMapOf(
            Pair('a', MaterialIngredient(*Tag.PLANKS.values.toTypedArray(), amount = 8)),
        )
    )
}
