package me.trqhxrd.grapesrpg.game.item.material

import me.trqhxrd.grapesrpg.game.item.attribute.Name
import me.trqhxrd.grapesrpg.impl.item.Item
import me.trqhxrd.grapesrpg.impl.recipe.crafting.ShapedRecipe
import me.trqhxrd.grapesrpg.impl.recipe.crafting.ShapelessRecipe
import me.trqhxrd.grapesrpg.impl.recipe.ingredient.MaterialIngredient
import org.bukkit.Material
import me.trqhxrd.grapesrpg.game.item.attribute.Material as MaterialAttr

class PlantFiber : Item(
    "plant_fiber",
    Material.STRING,
    mutableSetOf(
        Name("§bPlant Fiber"),
        MaterialAttr()
    )
) {
    init {
        this.recipe = Recipe(this)
    }

    private class Recipe(item: Item) : ShapelessRecipe(
        item,
        mutableListOf(
            MaterialIngredient(Material.GRASS, Material.TALL_GRASS, amount = 4),
            MaterialIngredient(Material.GRASS, Material.TALL_GRASS, amount = 4),
            MaterialIngredient(Material.GRASS, Material.TALL_GRASS, amount = 4)
        )
    )
}
