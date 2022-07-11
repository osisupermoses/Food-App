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
        icon = R.mipmap.on_boarding_icon_foreground,
        image = R.drawable.onboardingscreen,
        description = R.string.order
    )
    object Second: OnBoardingPage(
        icon = R.mipmap.on_boarding_icon_foreground,
        image = R.drawable.swift_delivery,
        description = R.string.deliver
    )
    object Third: OnBoardingPage(
        icon = R.mipmap.on_boarding_icon_foreground,
        image = R.drawable.eat_and_enjoy,
        description = R.string.eat
    )
}