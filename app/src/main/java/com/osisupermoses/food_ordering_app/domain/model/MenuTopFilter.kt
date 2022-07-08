package com.osisupermoses.food_ordering_app.domain.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.LocalPizza
import androidx.compose.ui.graphics.vector.ImageVector

data class MenuTopFilter(
    val icon: ImageVector? = null,
    val header: String
)

fun getMenuTopFilterBys(): List<MenuTopFilter> =
    listOf(
        MenuTopFilter(
            icon = null,
            header = "All"
        ),
        MenuTopFilter(
            icon = Icons.Rounded.LocalPizza,
            header = "Pizza"
        ),
        MenuTopFilter(
            icon = Icons.Rounded.LocalPizza,
            header = "Burgers"
        ),
        MenuTopFilter(
            icon = Icons.Rounded.LocalPizza,
            header = "Swallows"
        ),
        MenuTopFilter(
            icon = Icons.Rounded.LocalPizza,
            header = "Crunches"
        ),
    )
