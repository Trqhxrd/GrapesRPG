package me.trqhxrd.grapesrpg.game.item.weapon.sword

import me.trqhxrd.grapesrpg.game.item.attribute.Damaging
import me.trqhxrd.grapesrpg.game.item.attribute.Durability
import me.trqhxrd.grapesrpg.game.item.attribute.Name
import me.trqhxrd.grapesrpg.impl.item.Item
import me.trqhxrd.grapesrpg.impl.recipe.crafting.ShapedRecipe
import me.trqhxrd.grapesrpg.impl.recipe.ingredient.MaterialIngredient
import org.bukkit.Material

class StoneSword : Item(
    "grapes",
    "stone_sword",
    Material.STONE_SWORD,
    mutableSetOf(
        Damaging(5),
        Durability(128),
        Name("§bStone Sword")
    )
) {
    init {
        this.recipe = Recipe(this)
    }

    private class Recipe(item: me.trqhxrd.grapesrpg.api.item.Item) : ShapedRecipe(
        item,
        "_a__a__b_",
        mutableMapOf(
            Pair(
                'a',
                MaterialIngredient(Material.COBBLED_DEEPSLATE, Material.COBBLESTONE, Material.BLACKSTONE, amount = 4)
            ),
            Pair('b', MaterialIngredient(Material.STICK, amount = 4))
        )
    )
}
