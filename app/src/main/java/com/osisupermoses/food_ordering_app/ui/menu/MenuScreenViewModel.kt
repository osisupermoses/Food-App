package com.osisupermoses.food_ordering_app.ui.menu

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.osisupermoses.food_ordering_app.common.Resource
import com.osisupermoses.food_ordering_app.domain.repository.FoodOrderingRepository
import com.osisupermoses.food_ordering_app.util.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class MenuScreenViewModel @Inject constructor(
    private val repository: FoodOrderingRepository
) : ViewModel() {

    val state = mutableStateOf(MenuState())

    private val _errorChannel = Channel<UiText>()
    val errorChannel = _errorChannel.receiveAsFlow()

    val topClickIndex = mutableStateOf(0)
    val hasGoldBackground = mutableStateOf(false)

    init {
        getFoods()
        getRestaurants()
    }

    private fun getFoods() {
//        state.value = MenuState(isLoading = true)
        repository.getFoodList().onEach { response ->
            when(response) {
                is Resource.Success -> {
                    state.value = MenuState(
                        isLoading = false,
                        foodList = response.data
                    )
                }
                is Resource.Loading -> {
                    state.value = MenuState(isLoading = true)
                }
                is Resource.Error -> {
                    state.value = MenuState(
                        isLoading = false,
                        error = response.message ?: "Unknown Error"
                    )
                    _errorChannel.send(UiText.DynamicString(state.value.error))
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun getRestaurants() {
        repository.getRestaurantList().onEach { response ->
            when(response) {
                is Resource.Success -> {
                    state.value = MenuState(
                        isLoading = false,
                        restaurantList = response.data
                    )
                }
                is Resource.Loading -> {
                    state.value = MenuState(isLoading = true)
                }
                is Resource.Error -> {
                    state.value = MenuState(
                        isLoading = false,
                        error = response.message ?: "Unknown Error"
                    )
                    _errorChannel.send(UiText.DynamicString(state.value.error))
                }
            }
        }.launchIn(viewModelScope)
    }
}