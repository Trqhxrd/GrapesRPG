package me.trqhxrd.grapesrpg.game.item.weapons.swords

import me.trqhxrd.grapesrpg.game.item.attribute.Damaging
import me.trqhxrd.grapesrpg.game.item.attribute.Durability
import me.trqhxrd.grapesrpg.game.item.attribute.Name
import me.trqhxrd.grapesrpg.impl.item.Item
import org.bukkit.Material

class IronSword : Item(
    "iron_sword",
    Material.IRON_SWORD,
    mutableSetOf(
        Damaging(6),
        Durability(256),
        Name("§cIron Sword")
    )
)
