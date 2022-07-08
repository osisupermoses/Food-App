package com.osisupermoses.food_ordering_app.ui.Login

sealed class LoginEvent {
    data class EnteredEmail(val value: String) : LoginEvent()
    data class EnteredPhoneNum(val value: String) : LoginEvent()
    data class EnteredPassword(val value: String) : LoginEvent()
    data class OnSwitchChanged(val value: Boolean) : LoginEvent()
    data class HasLoggedInOrFailed(val message: String = "") : LoginEvent()
}
