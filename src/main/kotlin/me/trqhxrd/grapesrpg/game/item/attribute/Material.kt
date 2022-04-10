package me.trqhxrd.grapesrpg.game.item.attribute

import me.trqhxrd.grapesrpg.impl.item.attribute.Attribute
import me.trqhxrd.grapesrpg.impl.item.lore.LoreEntry
import org.bukkit.inventory.ItemStack

class Material : Attribute("grapes", "material") {

    override fun generateLoreEntry(): LoreEntry {
        return LoreEntry("§7Material")
    }

    override fun read(item: ItemStack) {}

    override fun write(item: ItemStack) = item
}
