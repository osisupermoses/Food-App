package com.osisupermoses.food_ordering_app.util

import java.text.NumberFormat

fun parseNumberToCurrencyFormat(number: Double): String {
    val numberFormat = NumberFormat.getCurrencyInstance()
    numberFormat.maximumFractionDigits = 2
    val convert = numberFormat.format(number)
    return convert.removePrefix("$")
}