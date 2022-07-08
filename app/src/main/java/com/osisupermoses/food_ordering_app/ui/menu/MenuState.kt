package com.osisupermoses.food_ordering_app.ui.menu

import com.osisupermoses.food_ordering_app.domain.model.*

data class MenuState(
    val isLoading: Boolean = false,
    val error: String = "",
    val headerList: List<MenuTopFilter>? = getMenuTopFilterBys(),
    val foodList: List<Food>? = getFoods(),
    val restaurantList: List<Restaurant>? = null
)
