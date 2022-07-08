package com.osisupermoses.food_ordering_app.util

import android.content.Context
import android.widget.Toast

fun toasty(
    context: Context,
    message: String
) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}