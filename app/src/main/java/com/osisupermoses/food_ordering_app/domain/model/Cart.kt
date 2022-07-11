package com.osisupermoses.food_ordering_app.domain.model

/**
* A data model class for Cart item with required fields.
*/
data class CartItem(
    var id: String? = null,
    val product_owner_id: String = "",
    val product_id: String = "",
    val title: String = "",
    val price: String = "",
    val image: String = "",
    var cart_quantity: Pair<RecipesItem, String>,
    var stock_quantity: String = "",
    val restaurant_id: String = "",
)