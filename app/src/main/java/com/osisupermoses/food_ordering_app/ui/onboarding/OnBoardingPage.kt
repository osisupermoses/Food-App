package com.osisupermoses.food_ordering_app.ui.onboarding

import androidx.annotation.DrawableRes
import com.osisupermoses.food_ordering_app.R

sealed class OnBoardingPage(
    @DrawableRes val icon: Int? = null,
    @DrawableRes val image: Int,
    val title: Int? = null,
    val description: Int
) {
    object First: OnBoardingPage(
        icon = null,
        image = R.drawable.onboardingscreen,
        description = R.string.order
    )
    object Second: OnBoardingPage(
        icon = null,
        image = R.drawable.onboardingscreen2,
        description = R.string.deliver
    )
    object Third: OnBoardingPage(
        icon = null,
        image = R.drawable.onboardingscreen3,
        description = R.string.eat
    )
}