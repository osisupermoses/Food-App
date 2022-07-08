package com.osisupermoses.food_ordering_app.ui.sign_up

sealed class SignUpEvent {
    data class EnteredFullName(val value: String) : SignUpEvent()
    data class EnteredEmail(val value: String) : SignUpEvent()
    data class EnteredPassword(val value: String) : SignUpEvent()
    data class OnCheckedChanged(val value: Boolean) : SignUpEvent()
    data class EnteredPhoneNumber(val value: String) : SignUpEvent()
}
