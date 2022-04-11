package me.trqhxrd.grapesrpg.game.item.weapons.swords

import me.trqhxrd.grapesrpg.game.item.attribute.Damaging
import me.trqhxrd.grapesrpg.game.item.attribute.Durability
import me.trqhxrd.grapesrpg.game.item.attribute.Name
import me.trqhxrd.grapesrpg.impl.item.Item
import org.bukkit.Material

class GoldSword : Item(
    "gold_sword",
    Material.GOLDEN_SWORD,
    mutableSetOf(
        Damaging(7),
        Durability(32),
        Name("§6Gold Sword")
    )
)
