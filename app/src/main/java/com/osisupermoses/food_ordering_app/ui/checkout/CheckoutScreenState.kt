package com.osisupermoses.food_ordering_app.ui.checkout

import com.osisupermoses.food_ordering_app.domain.model.Card

data class CheckoutScreenState(
    val isLoading: Boolean = false,
    val error: String? = null,
)
