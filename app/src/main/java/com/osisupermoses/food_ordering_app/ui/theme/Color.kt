package com.osisupermoses.food_ordering_app.ui.theme

import androidx.compose.material.Colors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver

val Purple200 = Color(0xFFBB86FC)
val Purple500 = Color(0xFF6200EE)
val Purple700 = Color(0xFF3700B3)
val Teal200 = Color(0xFF03DAC5)

val dimRed = Color(0xffDD0A35)
val GoldYellow = Color(0xFFFFD700)
val background = Color(0xFF2B292B)
val background800 = Color(0xFF424242)
val background900 = Color(0xFF212121)
val white87 = Color(0Xddffffff)

val SuccessColor = Color(0xff042E46)
val ErrorColor = Color(0xffDD0A35)
val InfoColor = Color(0xFF234F9D)

val masterCardRed = Color(0xffEB001B)
val masterCardYellow = Color(0xffF79E1B)
val masterCardOrange = Color(0xffFF5F00)

val visaCardColor = Color(0xff1A1F71)

val darkPrimary = Color(0xff242316)

val blue200 = Color(0xff91a4fc)
val Green500 = Color(0xFF1EB980)
val DarkBlue900 = Color(0xFF26282F)
val orangeError = Color(0xFFF94701)

@Composable
fun Colors.compositedOnSurface(alpha: Float): Color {
    return onSurface.copy(alpha = alpha).compositeOver(surface)
}

@Composable
fun Colors.randomBackgroundColor(): Color {
    val colors: List<Color> = listOf(
        Color(0xFFD0EFB3),
        Color(0xFFC0D6E3),
        Color(0xFFD8D8D8),
        Color(0xFFEE7B7E),
        Color(0xFFFFD48F),
        Color(0xFFD8D8D8)
    )

    return colors.random()
}
