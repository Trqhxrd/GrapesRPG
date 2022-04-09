package me.trqhxrd.grapesrpg.api.item.attribute

import me.trqhxrd.grapesrpg.api.item.lore.LoreEntry
import me.trqhxrd.grapesrpg.util.ModuleKey
import org.bukkit.inventory.ItemStack

interface Attribute {

    val moduleKey: ModuleKey

    fun generateLoreEntry(): LoreEntry?

    fun apply(item: ItemStack): ItemStack

    fun read(item: ItemStack)

    fun write(item: ItemStack): ItemStack
}
