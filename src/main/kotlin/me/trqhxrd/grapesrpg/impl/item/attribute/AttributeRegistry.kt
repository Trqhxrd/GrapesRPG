package me.trqhxrd.grapesrpg.impl.item.attribute

import me.trqhxrd.grapesrpg.api.item.attribute.Attribute
import me.trqhxrd.grapesrpg.util.ModuleKey
import kotlin.reflect.KClass

class AttributeRegistry(override val attributes: MutableSet<Attribute> = HashSet()) :
    me.trqhxrd.grapesrpg.api.item.attribute.AttributeRegistry {

    override fun getAttribute(key: ModuleKey): KClass<out Attribute> {
        return this.attributes.stream()
            .filter { a -> a.moduleKey == key }
            .map { a -> a::class }
            .findAny()
            .orElseThrow()
    }

    override fun getAttribute(module: String, key: String): KClass<out Attribute> {
        return this.getAttribute(ModuleKey(module, key))
    }

    override fun addAttribute(attribute: Attribute): Boolean {
        return try {
            this.getAttribute(attribute.moduleKey)
            false
        } catch (ex: NoSuchElementException) {
            this.attributes.add(attribute)
            true
        }
    }
}
