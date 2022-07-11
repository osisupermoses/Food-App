package com.osisupermoses.food_ordering_app.ui.menu

import com.osisupermoses.food_ordering_app.domain.model.*

data class MenuState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val headerList: List<MenuTopFilter>? = getMenuTopFilterBys(),
)
