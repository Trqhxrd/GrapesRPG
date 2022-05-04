package me.trqhxrd.grapesrpg.game.recipe.crafting

import me.trqhxrd.grapesrpg.game.item.attribute.Name
import me.trqhxrd.grapesrpg.impl.item.Item
import me.trqhxrd.grapesrpg.impl.recipe.crafting.ShapelessRecipe
import me.trqhxrd.grapesrpg.impl.recipe.ingredient.MaterialIngredient
import org.bukkit.Material
import org.bukkit.Tag

class DebugItem : Item("debug", Material.DIAMOND, mutableSetOf(Name("§bDiamond"))) {
    init {
        this.recipe = Recipe(this)
    }

    private class Recipe(item: Item) : ShapelessRecipe(item) {
        init {
            this.addIngredient(MaterialIngredient(*Tag.DIAMOND_ORES.values.toTypedArray(), amount = 16))
        }
    }
}
