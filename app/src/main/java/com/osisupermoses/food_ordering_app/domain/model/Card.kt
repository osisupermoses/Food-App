package com.osisupermoses.food_ordering_app.domain.model

import androidx.annotation.DrawableRes
import com.osisupermoses.food_ordering_app.R

data class Card(
    val id: Int? = null,
    val cardHolder: String = "",
    val cardNumber: String = "123412342342342",
    val cardExpiry: String = "04/22",
    val cardCvv: String = "203",
    val cardIssuerIcon: Any? = R.drawable.mastercard_logo,
    val cardLast4digits: String = "5532"
)

enum class CardType(
    val title: String,
    @DrawableRes val image: Int,
) {
    None("", R.drawable.ic_visa_logo),
    Visa("visa", R.drawable.ic_visa_logo),
    MasterCard("MasterCard", R.drawable.mastercard_logo),
    Verve("Verve", R.drawable.verve)
}

fun getCards() = listOf(
    Card(
        id = 1,
        cardIssuerIcon = R.drawable.mastercard_logo,
        cardLast4digits = "2211"
    ),
    Card(
        id = 2,
        cardIssuerIcon = R.drawable.verve,
        cardLast4digits = "9572"
    ),
    Card(
        id = 3,
        cardIssuerIcon = R.drawable.visa,
        cardLast4digits = "0043"
    )
)