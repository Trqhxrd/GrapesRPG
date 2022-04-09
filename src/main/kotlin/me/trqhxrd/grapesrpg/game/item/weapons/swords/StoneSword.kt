package me.trqhxrd.grapesrpg.game.item.weapons.swords

import me.trqhxrd.grapesrpg.game.item.attribute.Damaging
import me.trqhxrd.grapesrpg.game.item.attribute.Durability
import me.trqhxrd.grapesrpg.game.item.attribute.Name
import me.trqhxrd.grapesrpg.impl.item.Item
import org.bukkit.Material

class StoneSword : Item(
    "grapes",
    "stone_sword",
    Material.STONE_SWORD,
    mutableSetOf(
        Damaging(5),
        Durability(128),
        Name("§bStone Sword")
    )
)
