package me.trqhxrd.grapesrpg.api.item.attribute

import me.trqhxrd.grapesrpg.util.ModuleKey
import kotlin.reflect.KClass

interface AttributeRegistry {

    val attributes: MutableSet<Attribute>

    fun getAttribute(key: ModuleKey): KClass<out Attribute>

    fun getAttribute(module: String, key: String): KClass<out Attribute>

    fun addAttribute(attribute: Attribute): Boolean
}
