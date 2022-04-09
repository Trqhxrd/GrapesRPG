package me.trqhxrd.grapesrpg.game.item.weapons.swords

import me.trqhxrd.grapesrpg.game.item.attribute.Damaging
import me.trqhxrd.grapesrpg.game.item.attribute.Durability
import me.trqhxrd.grapesrpg.game.item.attribute.Name
import me.trqhxrd.grapesrpg.impl.item.Item
import org.bukkit.Material

class WoodenSword : Item(
    "grapes",
    "wooden_sword",
    Material.WOODEN_SWORD,
    mutableSetOf(
        Damaging(4),
        Durability(64),
        Name("§aWooden Sword")
    )
)
