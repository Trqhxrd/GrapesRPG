package me.trqhxrd.grapesrpg.game.item.weapon.sword.wooden

import me.trqhxrd.grapesrpg.game.item.attribute.Damaging
import me.trqhxrd.grapesrpg.game.item.attribute.Durability
import me.trqhxrd.grapesrpg.game.item.attribute.Name
import me.trqhxrd.grapesrpg.game.item.attribute.Rarity
import me.trqhxrd.grapesrpg.game.item.material.fiber.FiberGuard
import me.trqhxrd.grapesrpg.game.item.material.fiber.FiberHandle
import me.trqhxrd.grapesrpg.impl.item.Item
import me.trqhxrd.grapesrpg.impl.recipe.crafting.ShapedRecipe
import me.trqhxrd.grapesrpg.impl.recipe.ingredient.ItemIngredient
import org.bukkit.Material

class WoodenSword : Item(
    "wooden_sword",
    Material.WOODEN_SWORD,
    mutableSetOf(
        Damaging(4),
        Durability(64),
        Name("Wooden Sword"),
        Rarity(Rarity.Value.COMMON)
    )
) {
    init {
        this.recipe = Recipe(this)
    }

    private class Recipe(item: WoodenSword) : ShapedRecipe(
        item,
        "_a__b__c_",
        mutableMapOf(
            Pair('a', ItemIngredient(WoodenBlade())),
            Pair('b', ItemIngredient(FiberGuard())),
            Pair('c', ItemIngredient(FiberHandle()))
        )
    )
}
