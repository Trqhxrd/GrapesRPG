package me.trqhxrd.grapesrpg.game.item.weapons.swords

import me.trqhxrd.grapesrpg.game.item.attribute.Damaging
import me.trqhxrd.grapesrpg.game.item.attribute.Durability
import me.trqhxrd.grapesrpg.game.item.attribute.Name
import me.trqhxrd.grapesrpg.impl.item.Item
import me.trqhxrd.grapesrpg.impl.recipe.crafting.ShapedRecipe
import me.trqhxrd.grapesrpg.impl.recipe.ingredient.MaterialIngredient
import org.bukkit.Material
import org.bukkit.Tag

class WoodenSword : Item(
    "grapes",
    "wooden_sword",
    Material.WOODEN_SWORD,
    mutableSetOf(
        Damaging(4),
        Durability(64),
        Name("§aWooden Sword")
    )
) {
    init {
        this.recipe = Recipe(this)
    }

    private class Recipe(item: WoodenSword) : ShapedRecipe(
        item,
        "_a__a__b_",
        mutableMapOf(
            Pair('a', MaterialIngredient(*Tag.PLANKS.values.toTypedArray(), amount = 4)),
            Pair('b', MaterialIngredient(Material.STICK, amount = 4))
        )
    )
}
