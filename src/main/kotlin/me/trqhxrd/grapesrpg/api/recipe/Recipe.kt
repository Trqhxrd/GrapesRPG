package me.trqhxrd.grapesrpg.api.recipe

import me.trqhxrd.grapesrpg.api.item.Item
import org.bukkit.inventory.ItemStack

interface Recipe {
    val result: Item
    fun check(matrix: Array<ItemStack?>): Boolean
    fun reduce(matrix: Array<ItemStack?>):Array<ItemStack?>
}
