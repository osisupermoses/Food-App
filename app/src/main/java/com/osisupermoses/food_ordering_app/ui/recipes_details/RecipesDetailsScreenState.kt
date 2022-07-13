package com.osisupermoses.food_ordering_app.ui.recipes_details

import com.osisupermoses.food_ordering_app.domain.model.RecipesItem

data class RecipesDetailsViewState(
    val isLoading: Boolean = false,
    val recipe: RecipesItem? = null,
    val error: String = "",
    val isEmpty: Boolean = false,
    val hasError: Boolean = false
)
