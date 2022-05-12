package me.trqhxrd.grapesrpg.game.item

import me.trqhxrd.grapesrpg.game.item.attribute.Block
import me.trqhxrd.grapesrpg.game.world.blockdata.CraftingTable
import me.trqhxrd.grapesrpg.impl.item.Item
import org.bukkit.Material

class DebugBlock : Item(
    "block",
    Material.CRAFTING_TABLE,
    mutableSetOf(
        Block(CraftingTable())
    )
)