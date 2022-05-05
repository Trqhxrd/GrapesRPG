package me.trqhxrd.grapesrpg.game.item.material.leather

import me.trqhxrd.grapesrpg.game.item.attribute.Name
import me.trqhxrd.grapesrpg.game.item.attribute.Rarity
import me.trqhxrd.grapesrpg.impl.item.Item
import org.bukkit.Material
import me.trqhxrd.grapesrpg.game.item.attribute.Material as MaterialAttr

class LeatherStrap : Item(
    "leather_strap",
    Material.LEATHER,
    mutableSetOf(
        MaterialAttr(),
        Name("Leather Strap"),
        Rarity(Rarity.Value.COMMON)
    )
) {
}
