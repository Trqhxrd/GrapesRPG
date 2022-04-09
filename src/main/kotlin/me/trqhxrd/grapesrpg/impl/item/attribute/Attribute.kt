package me.trqhxrd.grapesrpg.impl.item.attribute

import me.trqhxrd.grapesrpg.api.item.lore.LoreEntry
import me.trqhxrd.grapesrpg.util.ModuleKey
import org.bukkit.inventory.ItemStack

abstract class Attribute(
    override val moduleKey: ModuleKey,
) : me.trqhxrd.grapesrpg.api.item.attribute.Attribute {

    constructor(module: String, key: String) : this(ModuleKey(module, key))

    override fun apply(item: ItemStack) = item

    override fun generateLoreEntry(): LoreEntry? {
        return null
    }
}
