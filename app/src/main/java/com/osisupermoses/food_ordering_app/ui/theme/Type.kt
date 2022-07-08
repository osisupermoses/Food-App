package com.osisupermoses.food_ordering_app.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.osisupermoses.food_ordering_app.R


val Rubik = FontFamily(
    Font(R.font.rubik_regular, FontWeight.Normal),
    Font(R.font.rubik_medium, FontWeight.Medium),
    Font(R.font.rubik_bold, FontWeight.Bold),
    Font(R.font.rubik_black, FontWeight.Black),
    Font(R.font.rubik_light, FontWeight.Light),
    Font(R.font.rubik_semi_bold, FontWeight.SemiBold),
    Font(R.font.rubik_extra_bold, FontWeight.ExtraBold)
)

val ubuntu = FontFamily(
    Font(R.font.ubuntu_n, FontWeight.Normal),
    Font(R.font.ubuntu_m, FontWeight.Medium),
    Font(R.font.ubuntu_b, FontWeight.Bold),
//    Font(R.font.ubuntu_n, FontWeight.Normal),
//    Font(R.font.ubuntu_n, FontWeight.Normal),
//    Font(R.font.ubuntu_n, FontWeight.Normal),
)

val clanproBook = FontFamily(
    Font(R.font.clanpro_book, FontWeight.Normal),
    Font(R.font.clanpro_narrbook, FontWeight.Light),
    Font(R.font.clanpro_narrmedium, FontWeight.Medium),
)

// Set of Material typography styles to start with
val Typography = Typography(
    body1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    )
    /* Other default text styles to override
    button = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
    */
)

val RubikTypography = Typography(
    h1 = TextStyle(
        fontFamily = ubuntu,
        fontSize = 50.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 114.8.sp,
        color = Color(0xff042E46)
    ),
    h2 = TextStyle(
        fontFamily = ubuntu,
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 36.08.sp,
        color = Color(0xffDD0A35)
    ),
    h3 = TextStyle(
        fontFamily = ubuntu,
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 26.24.sp,
        color = Color(0xff042E46)
    ),
    h4 = TextStyle(
        fontFamily = ubuntu,
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 26.sp,
        color = Color(0xff042E46)
    ),
    h5 = TextStyle(
        fontFamily = Rubik,
        fontSize = 14.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 22.96.sp,
        color = Color(0xff042E46)
    ),
    h6 = TextStyle(
        fontFamily = Rubik,
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 18.96.sp,
        color = Color(0xffDD0A35)
    ),
    body1 = TextStyle(
        fontFamily = Rubik,
        fontSize = 28.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 45.92.sp,
        color = Color(0xff2A3256)
    ),
    body2 = TextStyle(
        fontFamily = Rubik,
        fontSize = 13.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 15.85.sp,
        color = Color(0xff555555)
    ),
    button = TextStyle(
        fontFamily = Rubik,
        fontSize = 14.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 16.sp,
        letterSpacing = 1.25.sp
    ),
    caption = TextStyle(
        fontFamily = Rubik,
        fontSize = 13.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 21.32.sp,
        color = Color(0xff555555)
    ),
    overline = TextStyle(
        fontFamily = Rubik,
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 19.68.sp,
        color = Color(0xff2A3256)
    ),
    subtitle1 = TextStyle(
        fontFamily = clanproBook,
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 19.68.sp,
        color = Color(0xff2A3256)
    ),
    subtitle2 = TextStyle(
        fontFamily = clanproBook,
        fontSize = 10.sp,
        fontWeight = FontWeight.Light,
        lineHeight = 19.68.sp,
        color = Color.Gray
    )
)

// set of dark material typography styles to start with.
val DarkTypography = Typography(
    h1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        color = Color.White,
        fontSize = 28.sp
    ),
    h2 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        color = Color.White,
        fontSize = 21.sp
    ),
    body1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        color = Color.White,
        fontSize = 14.sp
    ),
    body2 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        color = white87,
        fontSize = 14.sp
    ),
    subtitle1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    subtitle2 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        color = Color.Gray
    )
)

// set of light material typography styles to start with.
val LightTypography = Typography(
    h1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        color = background900,
        fontSize = 28.sp
    ),
    h2 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        color = background900,
        fontSize = 21.sp
    ),
    body1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        color = background800,
        fontSize = 14.sp
    ),
    body2 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        color = background800,
        fontSize = 14.sp
    ),
    subtitle1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    subtitle2 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        color = Color.Gray
    )
)

val black15 = TextStyle(
    color = Color.Black,
    fontSize = 15.sp,
)