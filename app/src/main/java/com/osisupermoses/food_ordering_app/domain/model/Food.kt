package com.osisupermoses.food_ordering_app.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Food(
    val id: Long? = null,
    val name: String = "",
    val price: Double? = null,
    val image: String = "",
    val orderDescription: String = "",
    val orderRating: Double? = null,
    val isFavourite: Boolean = false,
    val foodCategory: String? = null,
    val estDeliveryTime: String = "45 - 120",
    val recipesItem: RecipesItem? = null,
    val userId: String = ""
)

fun getFoods(): List<Food> =
    listOf(
        Food(
            id = 1,
            name = "Dominos Pizza",
            price = 2500.00,
//            image = listOf(R.drawable.dominos_pizza),
            orderRating = 4.5,
            foodCategory = "Snacks"
        ),
        Food(
            id = 2,
            name = "Dough",
            price = 1200.00,
//            image = listOf(R.drawable.dough_),
            orderRating = 4.2,
            foodCategory = "Snacks"
        ),
        Food(
            id = 3,
            name = "TechieHub Pizza",
            price = 1000.00,
//            image = listOf(R.drawable.pizza2),
            orderRating = 4.0,
            foodCategory = "Snacks"
        )
    )