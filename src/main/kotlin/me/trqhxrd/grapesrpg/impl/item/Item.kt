package me.trqhxrd.grapesrpg.impl.item

import com.google.gson.reflect.TypeToken
import de.tr7zw.changeme.nbtapi.NBTItem
import me.trqhxrd.grapesrpg.GrapesRPG
import me.trqhxrd.grapesrpg.api.item.attribute.Attribute
import me.trqhxrd.grapesrpg.api.recipe.Recipe
import me.trqhxrd.grapesrpg.util.ModuleKey
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import java.util.concurrent.atomic.AtomicReference
import java.util.stream.Collectors
import kotlin.reflect.KClass
import me.trqhxrd.grapesrpg.api.item.Item as ItemAPI

open class Item(
    override val key: ModuleKey,
    override val type: Material,
    override val attributes: MutableSet<Attribute> = HashSet(),
    override var recipe: Recipe? = null
) : ItemAPI {

    constructor(
        key: String,
        type: Material,
        attributes: MutableSet<Attribute> = HashSet(),
        recipe: Recipe? = null
    ) : this(
        "grapes",
        key,
        type,
        attributes,
        recipe
    )

    constructor(
        module: String,
        key: String,
        type: Material,
        attributes: MutableSet<Attribute> = HashSet(),
        recipe: Recipe? = null
    ) : this(
        ModuleKey(module, key),
        type,
        attributes,
        recipe
    )

    companion object {
        fun isGrapesItem(itemStack: ItemStack): Boolean {
            return if (itemStack.type == Material.AIR) false
            else {
                val nbt = NBTItem(itemStack).addCompound("grapes")
                ModuleKey.deserializeOrNull(nbt.getString("id")) != null
            }
        }

        fun fromItemStack(itemStack: ItemStack): Item {
            val nbt = NBTItem(itemStack).addCompound("grapes")
            val key = ModuleKey.deserialize(nbt.getString("id"))
            val item = Item(key, itemStack.type)

            val attributeKeyStrings: MutableSet<String> =
                GrapesRPG.gson.fromJson(
                    nbt.getString("attributes"),
                    object : TypeToken<MutableSet<String>>() {}.type
                )
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
            .map { a -> a.moduleKey.toJson() }
            .collect(Collectors.toSet())
        val nbt = NBTItem(item)
        nbt.addCompound("grapes")
            .setObject("attributes", types)
        ref.set(nbt.item)
        this.attributes.forEach { a -> ref.set(a.write(ref.get())) }

        val lore = mutableListOf<String>()

        this.attributes.stream()
            .peek { a -> ref.set(a.apply(ref.get())) }
            .map { a -> a.generateLoreEntry() }
            .filter { a -> a != null }
            .sorted()
            .forEachOrdered { le -> le!!.append(lore) }

        val meta = ref.get().itemMeta ?: return ref.get()
        meta.lore = lore
        ref.get().itemMeta = meta

        return ref.get()
    }

    override fun build(): ItemStack {
        var item = ItemStack(this.type)

        val nbt = NBTItem(item)
        nbt.addCompound("grapes").setString("id", this.key.toJson())
        item = nbt.item

        item = this.applyAttributes(item)

        val meta = item.itemMeta
        val lore = meta!!.lore ?: mutableListOf()
        lore.add("§8${this.key.toJson()}")
        meta.lore = lore
        item.itemMeta = meta

        return item
    }

    override fun equalType(item: ItemAPI) = this.key == item.key
}
