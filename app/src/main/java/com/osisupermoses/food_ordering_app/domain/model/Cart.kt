package com.osisupermoses.food_ordering_app.domain.model

import com.google.firebase.firestore.PropertyName
import kotlinx.serialization.Serializable

/**
* A data model class for Cart item with required fields.
*/
@Serializable
data class CartItem(
    var id: String? = null,
    val product_owner_id: String = "",
    val product_id: String = "",
    val title: String = "",
    @get:PropertyName("cartPrice")
    @set:PropertyName("cartPrice")
    var cartPrice: String = "",
    val unitPrice: String = "",
    val image: String = "",
    val cartLimit: String = "",
    @get:PropertyName("cart_quantity")
    @set:PropertyName("cart_quantity")
    var cart_quantity: String = "1",
    val stock_quantity: String = "",
    val restaurant_id: String = "",
    val userId: String = ""
)