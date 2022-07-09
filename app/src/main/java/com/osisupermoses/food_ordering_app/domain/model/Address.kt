package com.osisupermoses.food_ordering_app.domain.model

import com.google.firebase.firestore.PropertyName

data class Address(
    val id: String? = null,
    @get:PropertyName("address")
    @set:PropertyName("address")
    var address: String = "",
    @get:PropertyName("user_id")
    @set:PropertyName("user_id")
    var userId: String = ""
)