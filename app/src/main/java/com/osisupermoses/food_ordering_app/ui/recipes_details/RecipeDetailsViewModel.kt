package com.osisupermoses.food_ordering_app.ui.recipes_details

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.osisupermoses.food_ordering_app.common.Constants
import com.osisupermoses.food_ordering_app.common.Resource
import com.osisupermoses.food_ordering_app.data.pref.repository.DataStoreRepository
import com.osisupermoses.food_ordering_app.domain.model.RecipesItem
import com.osisupermoses.food_ordering_app.domain.repository.AuthRepository
import com.osisupermoses.food_ordering_app.domain.repository.FoodOrderingRepository
import com.osisupermoses.food_ordering_app.ui.menu.MenuState
import com.osisupermoses.food_ordering_app.util.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeDetailsViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val dataStoreRepository: DataStoreRepository,
    private val repository: FoodOrderingRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val TAG = "RECIPEDETAILSVIEWMODEL"

    private val _state = MutableStateFlow(RecipesDetailsViewState())
    val viewState: StateFlow<RecipesDetailsViewState>
        get() = _state

    private val _errorChannel = Channel<UiText>()
    val errorChannel = _errorChannel.receiveAsFlow()

    private val foodId = savedStateHandle.get<Long>(Constants.FOOD_ID)!!

    init {
        viewModelScope.launch {
            getRecipeDetails(foodId)
        }
    }

//    private fun getRecipeDetails(id: Long) {
////        _state.value = RecipesDetailsViewState(isLoading = true)
//        viewModelScope.launch {
//            when (val recipeDetail = repository.getRecipeDetails(id)) {
//                is Resource.Error -> {
//                    _state.value = RecipesDetailsViewState(hasError = true)
//                }
//                else -> _state.value = if (recipeDetail.data != null) {
//                    RecipesDetailsViewState(recipe = recipeDetail.data)
//                } else {
//                    RecipesDetailsViewState(isEmpty = true)
//                }
//            }
////            _state.value = RecipesDetailsViewState(isLoading = false)
//        }
//    }

    fun getRecipeDetailsImages(recipesItem: RecipesItem): List<String> {
        return dataStoreRepository.getRecipeDetailsImages(recipesItem)
    }

    private suspend fun getRecipeDetails(id: Long) {
        _state.value = RecipesDetailsViewState(isLoading = true)
        authRepository.getRestaurantInfoFromFirestore().onEach { response ->
            when (response) {
                is Resource.Success -> {
                    _state.value = RecipesDetailsViewState(
                        isLoading = false,
                        recipe = response.data?.flatMap { it.food!! }?.map { it.recipesItem }
                            ?.first { it?.id == id }
                    )
                    Log.i(TAG, "RESTAURANTS: ${response.data}")
                }
                is Resource.Error -> {
                    _state.value = RecipesDetailsViewState(
                        isLoading = false,
                        error = response.message ?: "Unknown Error"
                    )
                    Log.i(TAG, "RESTAURANTS: ${response.message!!}")
                    _errorChannel.send(UiText.DynamicString(response.message.toString()))
                }
                else -> Unit
            }
        }.launchIn(viewModelScope)
    }

    fun saveRecipe(recipesItem: RecipesItem) {
        viewModelScope.launch {
//            saveRecipeUseCase(recipesItem)
        }
    }
}
