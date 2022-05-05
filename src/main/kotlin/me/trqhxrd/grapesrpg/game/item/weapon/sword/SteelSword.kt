package me.trqhxrd.grapesrpg.game.item.weapon.sword

import me.trqhxrd.grapesrpg.game.item.attribute.Damaging
import me.trqhxrd.grapesrpg.game.item.attribute.Durability
import me.trqhxrd.grapesrpg.game.item.attribute.Name
import me.trqhxrd.grapesrpg.impl.item.Item
import org.bukkit.Material

class SteelSword : Item(
    "steel_sword",
    Material.IRON_SWORD,
    mutableSetOf(
        Damaging(7),
        Durability(512),
        Name("§7Steel Sword")
    )
)
