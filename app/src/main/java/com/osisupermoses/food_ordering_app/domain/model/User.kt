package com.osisupermoses.food_ordering_app.domain.model

data class User(
    val id: String? = null,
    val userId: String = "",
    val fullName: String = "",
    val email: String = "",
    val isSellerAccount: String = "No",
    val phoneNumber: String = "",
    val profilePhoto: String = "",
    val userReview: String = ""
)