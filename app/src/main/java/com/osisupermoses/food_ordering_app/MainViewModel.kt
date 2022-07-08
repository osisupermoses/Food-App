package com.osisupermoses.food_ordering_app

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.osisupermoses.food_ordering_app.data.pref.repository.DataStoreRepository
import com.osisupermoses.food_ordering_app.navigation.Screens
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dataStoreRep: DataStoreRepository
) : ViewModel() {

    private val _startDestination = mutableStateOf<String?>(Screens.NoBottomBarScreens.WelcomeScreen.route)
    val startDestination: State<String?> = _startDestination

    init {
        onBoardingScreen()
    }

    private fun onBoardingScreen() {
        viewModelScope.launch(Dispatchers.Main) {
            dataStoreRep.readOnBoardingState().collectLatest { completed ->
                if (completed) {
                    _startDestination.value = Screens.NoBottomBarScreens.MenuScreen.route

                } else {
                    _startDestination.value = Screens.NoBottomBarScreens.WelcomeScreen.route
                }
            }
        }
    }
}
