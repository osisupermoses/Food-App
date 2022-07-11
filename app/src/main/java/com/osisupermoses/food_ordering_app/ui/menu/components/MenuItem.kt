package com.osisupermoses.food_ordering_app.ui.menu.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

data class MenuItem(
    val id: String,
    val title: String,
    val contentDescription: String,
    val icon: ImageVector
)

fun menuItems(): List<MenuItem> {
    return listOf(
        MenuItem(
            id = "home",
            title = "Home",
            contentDescription = "Go to home screen",
            icon = Icons.Default.Home
        ),
        MenuItem(
            id = "orders",
            title = "Orders",
            contentDescription = "Go to Orders Screen",
            icon = Icons.Default.ShoppingCart
        ),
        MenuItem(
            id = "admin",
            title = "Admin",
            contentDescription = "Go to Admin Screen",
            icon = Icons.Default.AdminPanelSettings
        ),
        MenuItem(
            id = "settings",
            title = "Settings",
            contentDescription = "Go to settings screen",
            icon = Icons.Default.Settings
        ),
        MenuItem(
            id = "help",
            title = "Help",
            contentDescription = "Get help",
            icon = Icons.Default.Info
        ),
    )
}
