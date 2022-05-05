package me.trqhxrd.grapesrpg.game.item.weapon.sword

import me.trqhxrd.grapesrpg.game.item.attribute.Damaging
import me.trqhxrd.grapesrpg.game.item.attribute.Durability
import me.trqhxrd.grapesrpg.game.item.attribute.Name
import me.trqhxrd.grapesrpg.impl.item.Item
import org.bukkit.Material

class DiamondSword : Item(
    "diamond_sword",
    Material.DIAMOND_SWORD,
    mutableSetOf(
        Damaging(7),
        Durability(1024),
        Name("§bDiamond Sword")
    )
)
