package me.trqhxrd.grapesrpg.api.recipe

interface ShapedRecipe : Recipe {
    val shape: CharArray
    val ingredients: MutableMap<Char, Ingredient>
    fun ingredient(slot: Int): Ingredient?
}
