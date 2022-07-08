package com.osisupermoses.food_ordering_app.data.pref

import androidx.datastore.preferences.core.booleanPreferencesKey

object PreferencesKeys {
    val onBoardingKey = booleanPreferencesKey(name = "on_boarding_completed")
}
