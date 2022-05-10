package me.trqhxrd.grapesrpg.gui.crafting

import me.trqhxrd.grapesrpg.GrapesRPG
import me.trqhxrd.grapesrpg.api.recipe.Recipe
import me.trqhxrd.grapesrpg.api.recipe.crafting.CraftingRecipe
import me.trqhxrd.menus.implementation.AbstractMenu
import me.wolfyscript.utilities.util.inventory.item_builder.ItemBuilder
import org.bukkit.Material.AIR
import org.bukkit.Material.GRAY_STAINED_GLASS_PANE
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType.*
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.inventory.InventoryView
import org.bukkit.inventory.ItemStack

class CraftingGUI() : AbstractMenu(SIZE, TITLE) {

    var recipe: CraftingRecipe? = null

    constructor(player: Player) : this() {
        this.open(player)
    }

    companion object {
        const val TITLE = "§8Crafting"
        const val SIZE = 5 * 9
        val INPUT_SLOTS = arrayOf(10, 11, 12, 19, 20, 21, 28, 29, 30)
        const val OUTPUT_SLOT = 24
        val BACKGROUND: ItemStack = ItemBuilder(GRAY_STAINED_GLASS_PANE).setDisplayName("§c").create()
    }

    override fun setup() {
        for (i in 0 until this.size) this.setStaticItem(i, BACKGROUND)
        for (i in INPUT_SLOTS) this.setStaticItem(i, ItemStack(AIR))
        this.setStaticItem(OUTPUT_SLOT, ItemStack(AIR))
    }

    override fun onClick(e: InventoryClickEvent) {
        if (e.clickedInventory == null) return
        e.isCancelled = true

        this.handleClick(e)
        this.handleRecipe(e.view)
    }

    override fun onClose(e: InventoryCloseEvent) {
        INPUT_SLOTS.mapNotNull { s -> e.view.topInventory.getItem(s) }
            .forEach { item -> e.player.location.world!!.dropItem(e.player.location, item) }
    }

    override fun onDrag(e: InventoryDragEvent) {
        val view = e.view
        val items = e.newItems.map { p -> p.value }

        for ((index, slot) in e.rawSlots.withIndex()) view.setItem(slot, items[index])
        view.cursor = e.cursor

        this.handleRecipe(e.view)
    }

    private fun handleClick(e: InventoryClickEvent) {
        e.isCancelled = true
        if (e.clickedInventory == null) return
        val rawSlot = e.rawSlot
        val click = e.click
        val view = e.view

        if (rawSlot < SIZE && !INPUT_SLOTS.contains(rawSlot) && OUTPUT_SLOT != rawSlot) return

        when (click) {
            DOUBLE_CLICK -> this.doubleClick(view, rawSlot)
            LEFT -> this.left(view, rawSlot)
            SHIFT_LEFT -> this.leftShift(view, rawSlot)
            RIGHT -> this.right(view, rawSlot)
            SHIFT_RIGHT -> this.rightShift(view, rawSlot)
            NUMBER_KEY -> this.keyboard(view, rawSlot, e.hotbarButton)
            DROP -> this.drop(e, view, rawSlot)
            CONTROL_DROP -> this.ctrlDrop(e, view, rawSlot)
            MIDDLE -> e.isCancelled = false
            else -> {}
        }
    }

    private fun drop(e: InventoryClickEvent, view: InventoryView, rawSlot: Int) {
        val slot = view.getItem(rawSlot) ?: air()
        if (slot.type == AIR) return

        this.drop0(e.whoClicked, slot)
        view.setItem(rawSlot, air())
        if (rawSlot == OUTPUT_SLOT) this.reduceRecipe(view)

        this.handleRecipe(view)
    }

    private fun ctrlDrop(e: InventoryClickEvent, view: InventoryView, rawSlot: Int) {
        if (this.recipe == null) return
        val r = this.recipe!!::class
        while (this.recipe != null && this.recipe!!::class != r) this.drop(e, view, rawSlot)
    }

    private fun drop0(e: LivingEntity, item: ItemStack) {
        val entity = e.world.dropItem(e.eyeLocation, item)
        entity.owner = e.uniqueId
        entity.pickupDelay = 30
        entity.thrower = e.uniqueId
        entity.velocity = e.eyeLocation.direction.normalize()
    }

    private fun doubleClick(view: InventoryView, rawSlot: Int) {
        val cursor = view.cursor ?: air()
        if (cursor.type == AIR) return

        val invPriority =
            if (rawSlot >= SIZE) arrayOf(view.bottomInventory, view.topInventory)
            else arrayOf(view.topInventory, view.bottomInventory)

        for (inv in invPriority) {
            val slots = if (inv == view.topInventory) INPUT_SLOTS.iterator() else (0 until SIZE).iterator()
            for (i in slots) {
                val slot = inv.getItem(i) ?: air()
                if (cursor.isSimilar(slot)) {
                    if (cursor.amount + slot.amount <= cursor.maxStackSize) {
                        cursor.amount += slot.amount
                        inv.setItem(i, air())
                    } else {
                        val diff = cursor.maxStackSize - cursor.amount
                        cursor.amount = cursor.maxStackSize
                        slot.amount -= diff
                    }
                }
            }
        }
    }

    private fun left(view: InventoryView, rawSlot: Int) {
        val slot = view.getItem(rawSlot) ?: this.air()
        val cursor = view.cursor ?: this.air()
        if (INPUT_SLOTS.contains(rawSlot) || rawSlot >= SIZE) {
            if (cursor.type == AIR && slot.type != AIR) {
                view.cursor = slot
                view.setItem(rawSlot, air())
            } else if (cursor.type != AIR && slot.type == AIR) {
                view.setItem(rawSlot, cursor)
                view.cursor = air()
            } else if (cursor.type != AIR && cursor.isSimilar(slot)) {
                if (cursor.amount + slot.amount <= slot.maxStackSize) {
                    slot.amount += cursor.amount
                    view.cursor = air()
                } else {
                    cursor.amount = cursor.amount + slot.amount - slot.maxStackSize
                    slot.amount = slot.maxStackSize
                }
            } else {
                view.cursor = slot
                view.setItem(rawSlot, cursor)
            }
        } else if (rawSlot == OUTPUT_SLOT) {
            if (cursor.type != AIR) {
                if (cursor.isSimilar(slot) && cursor.amount + slot.amount <= cursor.maxStackSize) {
                    cursor.amount += slot.amount
                    this.reduceRecipe(view)
                } else return
            } else if (cursor.type == AIR && slot.type != AIR) {
                view.cursor = slot
                view.setItem(rawSlot, air())
                this.reduceRecipe(view)
            }
        } else return
    }

    private fun leftShift(view: InventoryView, rawSlot: Int) {
        val slot = view.getItem(rawSlot) ?: this.air()

        if (INPUT_SLOTS.contains(rawSlot)) {
            val remaining =
                view.bottomInventory.addItem(slot).map { (_, item) -> item }.firstOrNull { i -> i.type != AIR } ?: air()
            view.setItem(rawSlot, remaining)
        } else if (rawSlot == OUTPUT_SLOT) {
            var recipe = this.handleRecipe(view)
            while (recipe != null) {
                this.reduceRecipe(view)
                val remaining = view.bottomInventory.addItem(slot).map { (_, item) -> item }.toSet()
                if (remaining.isNotEmpty()) {
                    val diff = recipe.result.build().amount - remaining.sumOf { item -> item.amount }
                    val result = recipe.result.build()
                    result.amount = diff
                    view.bottomInventory.removeItem(result)
                    break
                }

                recipe = this.handleRecipe(view)
            }
        } else if (rawSlot >= SIZE) {
            for (i in INPUT_SLOTS) {
                val content = view.getItem(i) ?: air()
                if (content.isSimilar(slot)) {
                    if (content.amount + slot.amount <= content.maxStackSize) {
                        content.amount += slot.amount
                        view.setItem(rawSlot, air())
                        return
                    } else {
                        val diff = content.maxStackSize - content.amount
                        content.amount = content.maxStackSize
                        if (diff < 1) view.setItem(rawSlot, air())
                        else slot.amount = diff
                    }
                }
            }
            for (i in INPUT_SLOTS) {
                val content = view.getItem(i) ?: air()
                if (content.type == AIR) {
                    view.setItem(i, slot)
                    view.setItem(rawSlot, air())
                    return
                }
            }
        }
    }

    private fun right(view: InventoryView, rawSlot: Int) {
        val slot = view.getItem(rawSlot) ?: this.air()
        val cursor = view.cursor ?: this.air()
        if (rawSlot != OUTPUT_SLOT) {
            if (cursor.type == AIR) {
                val split = this.splitStack(slot, 2)
                view.setItem(rawSlot, split[0])
                view.cursor = split[1]
            } else if (slot.type == AIR) {
                if (cursor.amount > 1) {
                    cursor.amount--
                    view.setItem(rawSlot, cursor.clone())
                    view.getItem(rawSlot)!!.amount = 1
                } else {
                    view.setItem(rawSlot, cursor.clone())
                    view.cursor = air()
                }
            } else if (slot.isSimilar(cursor)) {
                if (slot.amount < slot.maxStackSize) {
                    slot.amount++
                    if (cursor.amount > 1) cursor.amount--
                    else view.cursor = air()
                } else return
            } else return
        } else {
            if (cursor.type == AIR) {
                view.cursor = slot
                view.cursor = air()
            } else if (cursor.isSimilar(slot)) {
                if (cursor.amount + slot.amount <= cursor.maxStackSize) {
                    cursor.amount += slot.amount
                    view.setItem(rawSlot, air())
                } else return
            } else return
        }
    }

    private fun rightShift(view: InventoryView, rawSlot: Int) {
        this.leftShift(view, rawSlot)
    }

    private fun keyboard(view: InventoryView, rawSlot: Int, hotBarSlot: Int) {
        val slot = view.getItem(rawSlot) ?: this.air()
        val hotBar = view.bottomInventory.getItem(hotBarSlot) ?: this.air()

        if (rawSlot != OUTPUT_SLOT) {
            view.setItem(rawSlot, hotBar)
            view.bottomInventory.setItem(hotBarSlot, slot)
        } else if (hotBar.isSimilar(slot)) {
            if (hotBar.amount + slot.amount <= hotBar.maxStackSize) {
                hotBar.amount += slot.amount
                view.setItem(rawSlot, air())
                this.reduceRecipe(view)
            } else return
        } else if (hotBar.type == AIR) {
            view.bottomInventory.setItem(hotBarSlot, slot)
            view.setItem(rawSlot, air())
            this.reduceRecipe(view)
        } else return
    }

    private fun handleRecipe(view: InventoryView): Recipe? {
        val matrix = buildList {
            for (slot in INPUT_SLOTS) this.add(view.topInventory.getItem(slot))
        }.toTypedArray()

        if (matrix.size != INPUT_SLOTS.size) throw RuntimeException("Something went wrong while reading the recipe!")

        this.recipe = GrapesRPG.recipes
            .filterIsInstance<CraftingRecipe>()
            .firstOrNull() { r -> r.check(matrix) }
        if (this.recipe == null) view.topInventory.setItem(OUTPUT_SLOT, null)
        else view.topInventory.setItem(OUTPUT_SLOT, this.recipe!!.result.build())

        return this.recipe
    }

    private fun reduceRecipe(view: InventoryView) {
        if (this.recipe == null) return
        val matrix = INPUT_SLOTS.map { i -> view.getItem(i) }.toTypedArray()
        for ((index, item) in this.recipe!!.reduce(matrix).withIndex()) view.setItem(INPUT_SLOTS[index], item ?: air())
        this.handleRecipe(view)
    }

    private fun air() = ItemStack(AIR)

    @Suppress("UNCHECKED_CAST")
    private fun splitStack(item: ItemStack, stacks: Int): Array<ItemStack> {
        var left = item.amount % stacks
        val perStack = (item.amount - left) / stacks
        val items = arrayOfNulls<ItemStack>(stacks)

        items.fill(item)
        items.forEach { i ->
            if (left > 0) {
                i!!.amount = perStack + 1
                left--
            } else i!!.amount = perStack
        }

        return items as Array<ItemStack>
    }
}
