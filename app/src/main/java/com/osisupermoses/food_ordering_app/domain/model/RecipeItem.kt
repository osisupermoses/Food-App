package com.osisupermoses.food_ordering_app.domain.model

import com.osisupermoses.food_ordering_app.R

data class RecipesItem(
    val id: Int? = 1,
    val sustainable: Boolean? = null,
    val glutenFree: Boolean? = null,
    val veryPopular: Boolean? = null,
    val healthScore: Double? = null,
    val title: String? = null,
    val price: Double = 0.0,
    val deliveryFee: Double = 0.0,
    val total: Double = price + deliveryFee,
    val aggregateLikes: Int? = null,
    val creditsText: String? = null,
    val readyInMinutes: Int? = null,
    val dairyFree: Boolean? = null,
    val vegetarian: Boolean? = null,
    val foodCategory: String = "Sncaks",
    val totalStock: Long = 20,
    val quantityPurchased: Long = 2,
    val quantityInStock: Long = totalStock - quantityPurchased,
    val restaurantId: Long? = null,
    val image: Any? = null,
    val veryHealthy: Boolean? = null,
    val vegan: Boolean? = null,
    val cheap: Boolean? = null,
    val spoonacularScore: Double? = null,
    val sourceName: String? = null,
    val percentCarbs: Double? = null,
    val percentProtein: Double? = null,
    val percentFat: Double? = null,
    val nutrientsAmount: Double? = 0.0,
    val nutrientsName: String? = "",
    val servings: Int? = 0,
    val step: List<String>? = emptyList(),
    val ingredientOriginalString: List<String>? = emptyList(),
    var saved: Boolean = false
)

fun getRecipeItemList(): List<RecipesItem> =
    listOf(
        RecipesItem(
            id = 1,
            sustainable = true,
            glutenFree = true,
            veryPopular = true,
            healthScore = 9.00,
            title = "Dominos Pizza",
            aggregateLikes = 50,
            price = 2500.00,
            deliveryFee = 200.00,
            creditsText = "Awesome",
            readyInMinutes = 20,
            dairyFree = true,
            vegetarian = true,
            image = R.drawable.dominos_pizza,
            veryHealthy = true,
            vegan = false,
            cheap = true,
            spoonacularScore = 20.00,
            sourceName = "Pizza",
            percentCarbs = 3.11,
            percentProtein = 4.22,
            percentFat = 6.90,
            nutrientsAmount = 8.00,
            servings = 20,
            step = listOf("Unavailable at the moment"),
            ingredientOriginalString = listOf("Flour", "Pasta", "Jam"),
            saved = true
        ),
        RecipesItem(
            id = 2,
            sustainable = true,
            glutenFree = true,
            veryPopular = true,
            healthScore = 9.00,
            title = "Dough",
            aggregateLikes = 50,
            creditsText = "Awesome",
            readyInMinutes = 20,
            dairyFree = true,
            price = 1200.00,
            deliveryFee = 200.00,
            vegetarian = true,
            image = R.drawable.dough_,
            veryHealthy = true,
            vegan = false,
            cheap = true,
            spoonacularScore = 20.00,
            sourceName = "Pizza",
            percentCarbs = 3.11,
            percentProtein = 4.22,
            percentFat = 6.90,
            nutrientsAmount = 8.00,
            servings = 20,
            step = listOf("Unavailable at the moment"),
            ingredientOriginalString = listOf("Flour", "Pasta", "Jam"),
            saved = true
        ),
        RecipesItem(
            id = 3,
            sustainable = true,
            glutenFree = true,
            veryPopular = true,
            healthScore = 9.00,
            title = "TechieHub's Pizza",
            aggregateLikes = 50,
            creditsText = "Awesome",
            readyInMinutes = 20,
            dairyFree = true,
            vegetarian = true,
            price = 1000.00,
            deliveryFee = 200.00,
            image = R.drawable.pizza2,
            veryHealthy = true,
            vegan = false,
            cheap = true,
            spoonacularScore = 20.00,
            sourceName = "Pizza",
            percentCarbs = 3.11,
            percentProtein = 4.22,
            percentFat = 6.90,
            nutrientsAmount = 8.00,
            servings = 20,
            step = listOf("Unavailable at the moment"),
            ingredientOriginalString = listOf("Flour", "Pasta", "Jam"),
            saved = true
        ),

)