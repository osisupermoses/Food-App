package com.osisupermoses.food_ordering_app.ui.onboarding

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.osisupermoses.food_ordering_app.data.pref.repository.DataStoreRepository
import com.osisupermoses.food_ordering_app.domain.repository.AuthRepository
import com.osisupermoses.food_ordering_app.navigation.Screens
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class SplashViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val dataStoreRep: DataStoreRepository
) : ViewModel() {

    private val _isLoading: MutableState<Boolean> = mutableStateOf(true)
    val isLoading: State<Boolean> = _isLoading

    private val _startDestination: MutableState<String> = mutableStateOf(Screens.NoBottomBarScreens.WelcomeScreen.route)
    val startDestination: State<String> = _startDestination

    init {
        onBoardingScreen()
    }

    private fun onBoardingScreen() {
        viewModelScope.launch {
            dataStoreRep.readOnBoardingState().collectLatest { completed ->
                //checks if there's a Firebase User and has accessed the onboarding screen before,
                // if so it then takes them to NeworExistingScreen or WelcomeScreen
                if (!completed && !authRepository.isUserAuthenticatedInFirebase())
                    _startDestination.value = Screens.NoBottomBarScreens.WelcomeScreen.route
                else if (completed && !authRepository.isUserAuthenticatedInFirebase())
                    _startDestination.value = Screens.NoBottomBarScreens.NewOrExistingScreen.route
                else if (authRepository.isUserAuthenticatedInFirebase())
                    _startDestination.value = Screens.NoBottomBarScreens.MenuScreen.route
            }
            _isLoading.value = false
        }
    }
}
