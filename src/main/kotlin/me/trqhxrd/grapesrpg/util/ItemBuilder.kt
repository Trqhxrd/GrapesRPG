package me.trqhxrd.grapesrpg.util

import org.bukkit.Color
import org.bukkit.DyeColor
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.BannerMeta
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.inventory.meta.LeatherArmorMeta
import org.bukkit.inventory.meta.SkullMeta


class ItemBuilder {
    private var stack: ItemStack

    constructor(mat: Material?) {
        stack = ItemStack(mat!!)
    }

    constructor(mat: Material?, sh: Short) {
        stack = ItemStack(mat!!, 1, sh)
    }

    val itemMeta: ItemMeta?
        get() = stack.itemMeta

    fun setColor(color: Color?): ItemBuilder {
        val meta = stack.itemMeta as LeatherArmorMeta?
        meta!!.setColor(color)
        setItemMeta(meta)
        return this
    }

    fun setGlow(glow: Boolean): ItemBuilder {
        if (glow) {
            addEnchant(Enchantment.KNOCKBACK, 1)
            addItemFlag(ItemFlag.HIDE_ENCHANTS)
        } else {
            val meta = itemMeta
            for (enchantment in meta!!.enchants.keys) {
                meta.removeEnchant(enchantment!!)
            }
        }
        return this
    }

    fun setUnbreakable(unbreakable: Boolean): ItemBuilder {
        val meta = stack.itemMeta
        meta!!.isUnbreakable = unbreakable
        stack.itemMeta = meta
        return this
    }

    fun setBannerColor(color: DyeColor?): ItemBuilder {
        val meta = stack.itemMeta as BannerMeta?
        meta!!.baseColor = color
        setItemMeta(meta)
        return this
    }

    fun setAmount(amount: Int): ItemBuilder {
        stack.amount = amount
        return this
    }

    fun setItemMeta(meta: ItemMeta?): ItemBuilder {
        stack.itemMeta = meta
        return this
    }

    fun setHead(owner: String?): ItemBuilder {
        val meta = stack.itemMeta as SkullMeta?
        meta!!.owner = owner
        setItemMeta(meta)
        return this
    }

    fun setDisplayName(displayname: String?): ItemBuilder {
        val meta = itemMeta
        meta!!.setDisplayName(displayname)
        setItemMeta(meta)
        return this
    }

    fun setItemStack(stack: ItemStack): ItemBuilder {
        this.stack = stack
        return this
    }

    fun setLore(lore: ArrayList<String?>?): ItemBuilder {
        val meta = itemMeta
        meta!!.lore = lore
        setItemMeta(meta)
        return this
    }

    fun setLore(lore: String): ItemBuilder {
        val loreList = ArrayList<String>()
        loreList.add(lore)
        val meta = itemMeta
        meta!!.lore = loreList
        setItemMeta(meta)
        return this
    }

    fun addEnchant(enchantment: Enchantment?, level: Int): ItemBuilder {
        val meta = itemMeta
        meta!!.addEnchant(enchantment!!, level, true)
        setItemMeta(meta)
        return this
    }

    fun addItemFlag(flag: ItemFlag?): ItemBuilder {
        val meta = itemMeta
        meta!!.addItemFlags(flag)
        setItemMeta(meta)
        return this
    }

    fun build(): ItemStack {
        return stack
    }
}
