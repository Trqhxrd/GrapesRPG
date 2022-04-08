package me.trqhxrd.grapesrpg.impl.item

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import de.tr7zw.changeme.nbtapi.NBTItem
import me.trqhxrd.grapesrpg.GrapesRPG
import me.trqhxrd.grapesrpg.api.item.attribute.Attribute
import me.trqhxrd.grapesrpg.util.ModuleKey
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import java.util.concurrent.atomic.AtomicReference
import java.util.stream.Collectors
import kotlin.reflect.KClass

open class Item(
    override val key: ModuleKey,
    override val type: Material,
    override val attributes: MutableSet<Attribute> = HashSet()
) : me.trqhxrd.grapesrpg.api.item.Item {

    constructor(key: String, type: Material, attributes: MutableSet<Attribute> = HashSet()) : this(
        "grapes",
        key,
        type,
        attributes
    )

    constructor(module: String, key: String, type: Material, attributes: MutableSet<Attribute> = HashSet()) : this(
        ModuleKey(module, key),
        type,
        attributes
    )

    companion object {
        fun fromItemStack(itemStack: ItemStack): Item {
            val nbt = NBTItem(itemStack).addCompound("grapes")
            val key = ModuleKey.deserialize(nbt.getString("id"))
            val item = Item(key, itemStack.type)

            val attributeKeyStrings: MutableSet<String> = Gson().fromJson(nbt.getString("attributes"))
            val attributeKeys: MutableSet<ModuleKey> = attributeKeyStrings.stream()
                .map { k -> ModuleKey.deserialize(k) }
                .collect(Collectors.toSet())

            val attributeClasses = attributeKeys.stream()
                .map { k -> GrapesRPG.attributes.getAttribute(k) }
                .collect(Collectors.toSet())

            val attributes = mutableSetOf<Attribute>()

            for (cls in attributeClasses) {
                val classConstructor = cls.constructors.find { c -> c.parameters.isEmpty() }
                if (classConstructor != null) attributes.add(classConstructor.call())
                else throw NullPointerException("The attribute ${cls.qualifiedName} does not have an empty constructor!")
            }

            for (attribute in attributes) {
                item.attributes.add(attribute)
                attribute.read(itemStack)
            }

            return item
        }

        private inline fun <reified T> Gson.fromJson(json: String) = fromJson<T>(json, object : TypeToken<T>() {}.type)
    }

    override fun <T : Attribute> getAttribute(clazz: KClass<out T>): T? {
        @Suppress("UNCHECKED_CAST")
        for (attr in this.attributes)
            if (attr::class == clazz)
                return attr as T
        return null
    }

    override fun applyAttributes(item: ItemStack): ItemStack {
        val ref = AtomicReference(item)
        val types = this.attributes.stream()
            .map { a -> a.moduleKey.serialized }
            .collect(Collectors.toSet())
        val nbt = NBTItem(item)
        nbt.addCompound("grapes")
            .setObject("attributes", types)
        ref.set(nbt.item)
        this.attributes.forEach { a -> ref.set(a.write(ref.get())) }
        return ref.get()
    }

    override fun build(): ItemStack {
        var item = ItemStack(this.type)

        val nbt = NBTItem(item)
        nbt.addCompound("grapes").setString("id", this.key.serialized)
        item = nbt.item

        item = this.applyAttributes(item)

        return item
    }

    override fun equalType(item: me.trqhxrd.grapesrpg.api.item.Item) = this.key == item.key
}
