package me.trqhxrd.grapesrpg.impl.recipe

import me.trqhxrd.grapesrpg.api.item.Item
import me.trqhxrd.grapesrpg.api.recipe.CraftingMenu
import me.trqhxrd.grapesrpg.api.recipe.Ingredient
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class ShapedRecipe(
    override val result: Item,
    override val bindings: MutableMap<Ingredient, Int>,
    override val shape: CharArray,
    override val ingredients: MutableMap<Char, Ingredient>
) : me.trqhxrd.grapesrpg.api.recipe.ShapedRecipe {

    override fun check(matrix: Array<ItemStack>, bindings: Array<ItemStack>): Boolean {
        var b = true

        // Check matrix
        for (i in matrix.indices) {
            val item = matrix[i]
            val ingredient = this.ingredient(i)
            if (ingredient != null && !ingredient.accept(item) || ingredient == null && item.type != Material.AIR) {
                b = false
                break
            }
        }

        val compressedBindings = mutableListOf<ItemStack>()
        //Compress bindings
        for (binding in bindings) {
            if (compressedBindings.isEmpty()) {
                compressedBindings.add(binding)
                continue
            }
            for (toCheck in compressedBindings) {
                if (toCheck.isSimilar(binding)) {
                    if (toCheck.amount + binding.amount > toCheck.maxStackSize) {
                        val left = toCheck.amount + binding.amount - toCheck.maxStackSize
                        val other = binding.clone()
                        other.amount = left
                        compressedBindings.add(other)
                        toCheck.amount = toCheck.maxStackSize
                    } else toCheck.amount += binding.amount
                    break
                }
            }
        }

        // Check bindings
        val required = HashMap(this.bindings)
        val given = ArrayList(compressedBindings)

        for (item in given) {
            for (binding in required) {
                if (binding.key.accept(item)) {
                    when {
                        item.amount > binding.value -> {
                            item.amount -= binding.value
                            required.remove(binding.key)
                        }
                        item.amount == binding.value -> {
                            given.remove(item)
                            required.remove(binding.key)
                        }
                        item.amount < binding.value -> {
                            required[binding.key] = item.amount
                            given.remove(item)
                        }
                    }
                }
            }
        }

        if (required.isNotEmpty()) b = false

        return b
    }

    override fun run(menu: CraftingMenu) {
        val matrix = menu.matrix
        val bindings = menu.bindings

        if (!this.check(matrix, bindings)) return


    }

    override fun ingredient(slot: Int): Ingredient? {
        if (slot < 0 || slot > 9) throw ArrayIndexOutOfBoundsException(
            "Can't get ingredient for a slot, " +
                    "that isn't between 0 and 9. ($slot)"
        )
        return this.ingredients[this.shape[slot]]
    }
}
