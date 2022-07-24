package com.osisupermoses.food_ordering_app.domain.model

import kotlinx.serialization.Serializable

data class Order(
    val id: String = "",
    val items: ArrayList<CartItem> = ArrayList(),
    val address: Address = Address(),
    val title: String = "",
    val image: String = "",
    val sub_total_amount: String = "",
    val shipping_charge: String = "",
    val total_amount: String = "",
    val order_datetime: Long = 0L,
    val user_id: String = ""
)
