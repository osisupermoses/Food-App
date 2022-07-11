package com.osisupermoses.food_ordering_app.domain.model

import com.osisupermoses.food_ordering_app.R

data class Restaurant(
    val frontalImage: Any?,
    val restaurantId: Long? = null,
    val restaurantName: String,
    val restaurantDescription: String,
    val restaurantAddress: String = "8, Ojokondo Str, Agbowo, Ibadan, Nigeria",
    val restaurantReviews: Double = 4.8,
    val minOrderPrice: Double,
    val estDeliveryFeeRange: HashMap<Long, Long> = hashMapOf(200.toLong() to 500),
    val estResponseTime: HashMap<String, String> = hashMapOf("20" to "30"),
    val openingHours: HashMap<String, String> = hashMapOf("Monday - Sunday" to "09:00 - 21:00"),
    val paymentMethods: PaymentMethod = PaymentMethod(),
    val food: List<Food>? = null

)

data class PaymentMethod(
    val masterCard: String = "Mastercard",
    val visa: String = "Visa",
    val cash: String = "Cash on Delivery"
)

fun getRestaurants(): List<Restaurant> =
    listOf(
        Restaurant(
            frontalImage = R.drawable.ewa_agoyin_,
            restaurantId = 1,
            restaurantName = "Simi's Kitchen",
            restaurantDescription = "",
            minOrderPrice = 1000.00,
            food = getFoods()
        ),
        Restaurant(
            frontalImage = R.drawable.jollof_rice_and_chicken,
            restaurantId = 2,
            restaurantName = "Faith's Kitchen",
            restaurantDescription = "",
            minOrderPrice = 800.00,
            food = getFoods()
        ),
        Restaurant(
            frontalImage = R.drawable.efo_riro_,
            restaurantId = 3,
            restaurantName = "Peace's Kitchen",
            restaurantDescription = "",
            minOrderPrice = 800.00,
            food = getFoods()
        ),
    )