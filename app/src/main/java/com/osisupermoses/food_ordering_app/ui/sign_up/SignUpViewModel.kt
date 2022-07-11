package com.osisupermoses.food_ordering_app.ui.sign_up

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.osisupermoses.food_ordering_app.R
import com.osisupermoses.food_ordering_app.common.Constants
import com.osisupermoses.food_ordering_app.common.Resource
import com.osisupermoses.food_ordering_app.domain.model.User
import com.osisupermoses.food_ordering_app.domain.repository.AuthRepository
import com.osisupermoses.food_ordering_app.util.UiText
import com.osisupermoses.healthbloc.presentation.screens.register_user_screen.components.Country
import com.osisupermoses.healthbloc.presentation.screens.register_user_screen.components.getCountriesList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val mAuth = Firebase.auth

    private val _isLoggedIn = mutableStateOf(false)
    val isLoggedIn: State<Boolean> = _isLoggedIn

    private val _state = mutableStateOf(SignUpState())
    val state: State<SignUpState> = _state

    private val _errorChannel = Channel<UiText>()
    val errorChannel = _errorChannel.receiveAsFlow()

    private val _fullName = mutableStateOf("")
    val fullName: State<String> = _fullName

    private val _phoneNumber = mutableStateOf("")
    val phoneNumber: State<String> = _phoneNumber

    private val _isChecked = mutableStateOf(false)
    val isChecked: State<Boolean> = _isChecked

    val countriesList = getCountriesList()
    val mobileCountry = mutableStateOf(Country("ng", "234", "Nigeria"))

    private val _userEmail = mutableStateOf("")
    val userEmail: State<String> = _userEmail

    private val _password = mutableStateOf("")
    val password: State<String> = _password

    fun onEvent(event: SignUpEvent) {
        when(event) {
            is SignUpEvent.EnteredFullName -> {
                _fullName.value = event.value
            }
            is SignUpEvent.EnteredEmail -> {
                _userEmail.value = event.value
            }
            is SignUpEvent.EnteredPhoneNumber -> {
                if (event.value.length <= 11)
                _phoneNumber.value = event.value
            }
            is SignUpEvent.EnteredPassword -> {
                _password.value = event.value
            }
            is SignUpEvent.OnCheckedChanged -> {
                _isChecked.value = event.value
            }
        }
    }

    private fun register(nextScreen: () -> Unit) {
        viewModelScope.launch {
            repository.register(userEmail.value, password.value).collect { result ->
                when(result) {
                    is Resource.Loading -> {
                        _state.value = state.value.copy(isLoading = true)
                    }
                    is Resource.Success -> {
                        _state.value = state.value.copy(
                            isLoading = false,
                            data = result.data == true
                        )
                        if (result.data == true) {
                            nextScreen()
                            removeSpacesAndCreateUser()
                        }
                    }
                    is Resource.Error -> {
                        _state.value = state.value.copy(
                            isLoading = false,
                            error = result.message.toString()
                        )
                        _errorChannel.send(UiText.DynamicString(
                            result.message ?: "Something went wrong")
                        )
                    }
                }
            }
        }
    }

    fun isValidEntry(): Boolean {
        if (userEmail.value.isBlank() ||
            password.value.isBlank() ||
            fullName.value.isBlank() ||
            !isChecked.value
        ) {
            return false
        }
        return true
    }

    fun onWrongPasswordInput(nextScreen: () -> Unit) {
        viewModelScope.launch {
            when {
                _phoneNumber.value.isBlank() -> {
                    _errorChannel.send(UiText.StringResource(R.string.phone_number_cannot_be_blank))
                }
                _phoneNumber.value.length < 10 || _phoneNumber.value.length > 11 -> {
                    _errorChannel.send(UiText.StringResource(R.string.invalid_phone_number_please))
                }
                _phoneNumber.value.length == 11 && !_phoneNumber.value.startsWith("0") -> {
                    _errorChannel.send(UiText.StringResource(R.string.invalid_phone_number_please))
                }
                _password.value.length < 6 -> {
                    _errorChannel.send(UiText.StringResource(R.string.password_cannot_be_less_than_6_digits))
                }
                _phoneNumber.value.length == 11 && _phoneNumber.value.startsWith("0") -> {
                    val trimedPhoneNum = _phoneNumber.value.removePrefix("0")
                    _phoneNumber.value = trimedPhoneNum
                    nextScreen()
                }
                else -> register(nextScreen)
            }
        }
    }


    private fun removeSpacesAndCreateUser() {
        _userEmail.value = _userEmail.value.trim { it <= ' ' }
        _password.value = _password.value.trim { it <= ' ' }
        _fullName.value = _fullName.value.trimEnd().trimStart()
        createUser()
    }

    private fun createUser() {
        val userId = mAuth.currentUser
        val user = userId?.let {
            User(
                userId = userId.uid,
                fullName = _fullName.value,
                email = _userEmail.value
            )
        }
        saveToFirebase(user!!)
    }

    private fun saveToFirebase(user: User) {
        val db = FirebaseFirestore.getInstance()
        val dbCollection = db.collection(Constants.DB_Collection)

        if (user.toString().isNotEmpty()) {
            dbCollection.add(user)
                .addOnSuccessListener { documentRef ->
                    val docId = documentRef.id
                    dbCollection.document(docId)
                        .update(hashMapOf("id" to docId) as Map<String, Any>)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.i("FB", "User data successfully created: ")
                            }
                        }.addOnFailureListener {
                            Log.w("Error", "SaveToFirebase: Error updating doc", it)
                        }
                }
        }
    }
}