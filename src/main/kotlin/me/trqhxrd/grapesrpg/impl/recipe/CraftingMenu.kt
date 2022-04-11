package me.trqhxrd.grapesrpg.impl.recipe

import me.trqhxrd.menus.implementation.AbstractMenu
import org.bukkit.Material
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.inventory.ItemStack

class CraftingMenu(
    override var result: ItemStack? = null,
    override var matrix: Array<ItemStack> = arrayOf(),
    override var bindings: Array<ItemStack> = arrayOf()
) : AbstractMenu(54, "Crafting Table"), me.trqhxrd.grapesrpg.api.recipe.CraftingMenu {

    init {
        this.setup()
    }

    companion object {
        val background = ItemStack(Material.GRAY_STAINED_GLASS_PANE)

        init {
            val meta = background.itemMeta
            meta!!.setDisplayName("")
            background.itemMeta = meta
        }
    }

    override fun setup() {
        for (i in 0 until this.size) this.setStaticItem(i, background)
    }

    override fun onClick(e: InventoryClickEvent?) {
        if (e == null) return
        if (e.view.topInventory == e.clickedInventory) {
            if (e.slot)
        }
    }

    override fun onClose(e: InventoryCloseEvent?) {
    }

    override fun onDrag(e: InventoryDragEvent?) {
    }
}
