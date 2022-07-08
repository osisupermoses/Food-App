package com.osisupermoses.food_ordering_app.ui.recipes_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.osisupermoses.food_ordering_app.common.Constants
import com.osisupermoses.food_ordering_app.common.Resource
import com.osisupermoses.food_ordering_app.domain.model.RecipesItem
import com.osisupermoses.food_ordering_app.domain.repository.FoodOrderingRepository
import com.osisupermoses.food_ordering_app.util.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeDetailsViewModel @Inject constructor(
    private val repository: FoodOrderingRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(RecipesDetailsViewState())
    val viewState: StateFlow<RecipesDetailsViewState>
        get() = _state

    private val _errorChannel = Channel<UiText>()
    val errorChannel = _errorChannel.receiveAsFlow()

    private val foodId = savedStateHandle.get<Int>(Constants.FOOD_ID)!!

    init {
        getRecipeDetails(foodId)
    }

    private fun getRecipeDetails(id: Int) {
//        _state.value = RecipesDetailsViewState(isLoading = true)
        viewModelScope.launch {
            when (val recipeDetail = repository.getRecipeDetails(id)) {
                is Resource.Error -> {
                    _state.value = RecipesDetailsViewState(hasError = true)
                }
                else -> _state.value = if (recipeDetail.data != null) {
                    RecipesDetailsViewState(recipe = recipeDetail.data)
                } else {
                    RecipesDetailsViewState(isEmpty = true)
                }
            }
//            _state.value = RecipesDetailsViewState(isLoading = false)
        }
    }

    fun saveRecipe(recipesItem: RecipesItem) {
        viewModelScope.launch {
//            saveRecipeUseCase(recipesItem)
        }
    }
}
