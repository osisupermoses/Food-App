package com.osisupermoses.food_ordering_app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = Green500,
    secondary = Green500,
    onSecondary = Color.Black,
    surface = darkPrimary,
    background = background,
    onBackground = background800,
    primaryVariant = Purple500,
    onPrimary = Color.Black,
    onSurface = dimRed
)

private val LightColorPalette = lightColors(
    background = Color.White,
    onBackground = GoldYellow,
    surface = Color.White,
    primary = Purple200,
    primaryVariant = Purple500,
    secondary = Purple500,
    onPrimary = Color.White,
    onSecondary = Color.White,
    error = orangeError

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun Food_Ordering_AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {

    /**
     * support just dark color
     */
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    val typography = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    CompositionLocalProvider(LocalSpacing provides Spacing()) {
        MaterialTheme(
            colors = DarkColorPalette,
            typography = RubikTypography,
            shapes = Shapes,
            content = content
        )
    }
}