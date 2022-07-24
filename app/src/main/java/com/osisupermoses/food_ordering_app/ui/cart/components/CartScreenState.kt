package com.osisupermoses.food_ordering_app.ui.cart.components

import com.osisupermoses.food_ordering_app.domain.model.CartItem

data class CartScreenState(
    val isLoading: Boolean = false,
    val error: String = ""
)
