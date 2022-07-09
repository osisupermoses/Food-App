package com.osisupermoses.food_ordering_app.data.pref

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PreferencesKeys {
    val onBoardingKey = booleanPreferencesKey(name = "on_boarding_completed")
    val addressKey = stringPreferencesKey(name = "address")
}
