package me.trqhxrd.grapesrpg.api.recipe

interface ShapelessRecipe : Recipe {
    val ingredients: MutableList<Ingredient>
}
