package me.trqhxrd.grapesrpg.api.recipe

import org.bukkit.inventory.ItemStack

interface CraftingMenu {

    var result: ItemStack?
    var matrix: Array<ItemStack>
    var bindings: Array<ItemStack>
}
