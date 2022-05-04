package me.trqhxrd.grapesrpg.game.item.attribute

import me.trqhxrd.grapesrpg.impl.item.attribute.Attribute
import me.trqhxrd.grapesrpg.impl.item.lore.LoreEntry
import org.bukkit.inventory.ItemStack

class Todo : Attribute("grapes", "todo") {

    override fun generateLoreEntry(): LoreEntry {
        return LoreEntry("§cThis item is unfinished and has a chance of not working as expected")
    }

    override fun read(item: ItemStack) {}

    override fun write(item: ItemStack) = item
}
