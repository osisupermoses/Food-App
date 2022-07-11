package com.osisupermoses.food_ordering_app.domain.model

import com.osisupermoses.food_ordering_app.R

data class Food(
    val id: Int? = null,
    val name: String,
    val price: Double,
    val image: Any?,
    val orderDescription: String = "",
    val orderRating: Double,
    val isFavourite: Boolean = false,
    val foodCategory: String,
    val estDeliveryTime: String = "45 - 120",
    val restaurantId: Long? = null,
)

fun getFoods(): List<Food> =
    listOf(
        Food(
            id = 1,
            name = "Dominos Pizza",
            price = 2500.00,
            image = R.drawable.dominos_pizza,
            orderRating = 4.5,
            foodCategory = "Snacks"
        ),
        Food(
            id = 2,
            name = "Dough",
            price = 1200.00,
            image = R.drawable.dough_,
            orderRating = 4.2,
            foodCategory = "Snacks"
        ),
        Food(
            id = 3,
            name = "TechieHub Pizza",
            price = 1000.00,
            image = R.drawable.pizza2,
            orderRating = 4.0,
            foodCategory = "Snacks"
        ),
    )