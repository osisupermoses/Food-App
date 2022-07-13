package com.osisupermoses.food_ordering_app.ui.Login

import android.content.Context
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.osisupermoses.food_ordering_app.R
import com.osisupermoses.food_ordering_app.common.Constants
import com.osisupermoses.food_ordering_app.common.Resource
import com.osisupermoses.food_ordering_app.domain.model.User
import com.osisupermoses.food_ordering_app.domain.repository.AuthRepository
import com.osisupermoses.food_ordering_app.util.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


private const val TAG = "LoginViewModel"

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _isLoggedIn = mutableStateOf(false)
    val isLoggedIn: State<Boolean> = _isLoggedIn

    private val _state = mutableStateOf(LoginScreenState())
    val state: State<LoginScreenState> = _state

    private val _errorChannel = Channel<UiText>()
    val errorChannel = _errorChannel.receiveAsFlow()

    private val _switchChanged = mutableStateOf(false)
    val switchChanged: State<Boolean> = _switchChanged

    private val _userEmail = mutableStateOf("")
    val userEmail: State<String> = _userEmail

    private val _password = mutableStateOf("")
    val password: State<String> = _password

    init {
        viewModelScope.launch {
            delay(3000)
            _isLoggedIn.value = getCurrentUser() != null
        }
    }

    fun onEvent(event: LoginEvent) {
        when(event) {
            is LoginEvent.EnteredEmail -> {
                _userEmail.value = event.value
            }
            is LoginEvent.EnteredPassword -> {
                _password.value = event.value
            }
            is LoginEvent.OnSwitchChanged -> {
                _switchChanged.value = event.value
            }
            is LoginEvent.HasLoggedInOrFailed -> {
                viewModelScope.launch {
                    _state.value = LoginScreenState(isLoading = false)
                    _errorChannel.send(UiText.DynamicString(event.message))
                }
            }
            else -> {}
        }
    }


    private fun login(nextScreen: () -> Unit) {
        viewModelScope.launch {
            repository.login(userEmail.value, password.value).collect { result ->
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
                            nextScreen.invoke()
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

    fun loginWithGoogle(token: String, nextScreen: () -> Unit = {}) {
        viewModelScope.launch {
            repository.loginWithGoogle(token).collect { result ->
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
                            saveAuthenticatedUserIntoFirestore { nextScreen.invoke() }
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

    fun googleSignInClient(context: Context, token: String): GoogleSignInClient {
        _state.value = LoginScreenState(isLoading = true)
        // Configure Google Sign In
        val signInOptions = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(token)
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(context, signInOptions)
    }

    private fun getCurrentUser(): FirebaseUser? {
        val user = Firebase.auth.currentUser
        Log.d(TAG, "user display name: ${user?.displayName}, email: ${user?.email}")
        return user
    }

    fun onLoginBtnClick(nextScreen: () -> Unit) {
        viewModelScope.launch {
            when {
                _userEmail.value.isBlank() -> {
                    _errorChannel.send(UiText.StringResource(R.string.email_cannot_be_blank))
                }
                _password.value.isBlank() -> {
                    _errorChannel.send(UiText.StringResource(R.string.password_cannot_be_blank))
                }
                else -> login(nextScreen)
            }
        }
    }

    private fun saveAuthenticatedUserIntoFirestore(nextScreen: () -> Unit) {
        viewModelScope.launch {
            repository.getUserInfoFromFirestore().onEach { result ->
                when(result) {
                    is Resource.Loading -> {
                        _state.value = state.value.copy(isLoading = true)
                    }
                    is Resource.Success -> {
                        val currentUserId = result.data?.filter { user ->
                            user.userId == Firebase.auth.currentUser?.uid ||
                                    user.email == Firebase.auth.currentUser?.email
                        }

                        if (currentUserId.isNullOrEmpty()) {
                            createUser(nextScreen)
                        } else {
                            nextScreen()
                        }
                        _state.value = state.value.copy(isLoading = false)
                    }
                    is Resource.Error -> {
                        _errorChannel.send(UiText.DynamicString(result.message.toString()))
                    }
                }
            }.launchIn(this)
        }
    }

    private fun createUser(nextScreen: () -> Unit) {
        val currentUser = getCurrentUser()
        val user = currentUser?.let {
            User(
                userId = currentUser.uid,
                fullName = currentUser.displayName!!,
                email = currentUser.email!!
            )
        }
        saveUserToFirebase(user!!, nextScreen)
    }

    private fun saveUserToFirebase(user: User, nextScreen: () -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val dbCollection = db.collection(Constants.DB_Collection_Users)

        if (user.toString().isNotEmpty()) {
            dbCollection.add(user)
                .addOnSuccessListener { documentRef ->
                    val docId = documentRef.id
                    dbCollection.document(docId)
                        .update(hashMapOf("id" to docId) as Map<String, Any>)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                nextScreen()
                            }
                        }.addOnFailureListener {
                            Log.w("Error", "SaveToFirebase: Error updating doc", it)
                        }
                }
        }
    }

    sealed class UIEvent {
        data class ShowSnackbar(val message: String) : UIEvent()
    }
}