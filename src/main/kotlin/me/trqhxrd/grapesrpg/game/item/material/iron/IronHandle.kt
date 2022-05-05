package me.trqhxrd.grapesrpg.game.item.material.iron

import me.trqhxrd.grapesrpg.game.item.attribute.Name
import me.trqhxrd.grapesrpg.game.item.attribute.Rarity
import me.trqhxrd.grapesrpg.impl.item.Item
import org.bukkit.Material
import me.trqhxrd.grapesrpg.game.item.attribute.Material as MaterialAttr

class IronHandle: Item(
    "iron_handle",
    Material.IRON_INGOT,
    mutableSetOf(
        MaterialAttr(),
        Name("Iron handle"),
        Rarity(Rarity.Value.COMMON)
    )
) {
}
