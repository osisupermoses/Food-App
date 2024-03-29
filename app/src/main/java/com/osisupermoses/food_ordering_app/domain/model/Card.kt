package com.osisupermoses.food_ordering_app.domain.model

import androidx.annotation.DrawableRes
import com.google.firebase.firestore.PropertyName
import com.osisupermoses.food_ordering_app.R

data class Card(
    val id: String? = null,
    @get:PropertyName("user_id")
    @set:PropertyName("user_id")
    var userId: String = "",
    @get:PropertyName("card_holder")
    @set:PropertyName("card_holder")
    var cardHolder: String = "",
    @get:PropertyName("card_expiry")
    @set:PropertyName("card_expiry")
    var cardExpiry: String = "04/22",
    @get:PropertyName("card_cvv")
    @set:PropertyName("card_cvv")
    var cardCvv: String = "203",
    @get:PropertyName("card_issuer_icon")
    @set:PropertyName("card_issuer_icon")
    var cardIssuerIcon: Any? = R.drawable.mastercard_logo,
    @get:PropertyName("card_number")
    @set:PropertyName("card_number")
    var cardNumber: String = "55321234123411234"
)

enum class CardType(
    val title: String,
    @DrawableRes val image: Int
) {
    None("", R.drawable.ic_visa_logo),
    Visa("visa", R.drawable.ic_visa_logo),
    MasterCard("MasterCard", R.drawable.mastercard_logo),
    Verve("Verve", R.drawable.verve)
}

fun getCards() = listOf(
    Card(
        cardIssuerIcon = R.drawable.mastercard_logo,
        cardNumber = "2211"
    ),
    Card(
        cardIssuerIcon = R.drawable.verve,
        cardNumber = "9572"
    ),
    Card(
        cardIssuerIcon = R.drawable.ic_visa_logo,
        cardNumber = "0043"
    )
)