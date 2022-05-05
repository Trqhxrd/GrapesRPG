package me.trqhxrd.grapesrpg.game.item.weapon.sword

import me.trqhxrd.grapesrpg.game.item.attribute.Damaging
import me.trqhxrd.grapesrpg.game.item.attribute.Durability
import me.trqhxrd.grapesrpg.game.item.attribute.Name
import me.trqhxrd.grapesrpg.impl.item.Item
import org.bukkit.Material

class NetheriteSword : Item(
    "netherite_sword",
    Material.NETHERITE_SWORD,
    mutableSetOf(
        Damaging(8),
        Durability(2048),
        Name("§4Netherite Sword")
    )
)
