package me.trqhxrd.grapesrpg.api.item

import me.trqhxrd.grapesrpg.api.item.attribute.Attribute
import me.trqhxrd.grapesrpg.api.recipe.Recipe
import me.trqhxrd.grapesrpg.util.ModuleKey
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import kotlin.reflect.KClass

interface Item {

    val key: ModuleKey

    val type: Material

    val attributes: Set<Attribute>

    var recipe: Recipe?

    fun <T : Attribute> getAttribute(clazz: KClass<out T>): T?

    fun applyAttributes(item: ItemStack): ItemStack

    fun build(): ItemStack

    fun equalType(item: Item): Boolean
}
