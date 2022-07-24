package com.osisupermoses.food_ordering_app.ui.recipes_details

import com.osisupermoses.food_ordering_app.domain.model.RecipesItem

data class RecipesDetailsViewState(
    val isLoading: Boolean = false,
    val error: String = "",
)
