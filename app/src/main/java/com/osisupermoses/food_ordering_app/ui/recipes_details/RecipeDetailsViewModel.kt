package com.osisupermoses.food_ordering_app.ui.recipes_details

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.osisupermoses.food_ordering_app.R
import com.osisupermoses.food_ordering_app.common.Constants
import com.osisupermoses.food_ordering_app.common.Resource
import com.osisupermoses.food_ordering_app.data.pref.repository.DataStoreRepository
import com.osisupermoses.food_ordering_app.domain.model.Card
import com.osisupermoses.food_ordering_app.domain.model.CartItem
import com.osisupermoses.food_ordering_app.domain.model.RecipesItem
import com.osisupermoses.food_ordering_app.domain.repository.AuthRepository
import com.osisupermoses.food_ordering_app.domain.repository.FoodOrderingRepository
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

    var addToCart = mutableStateOf(R.string.add_to_cart)
    private val _errorChannel = Channel<UiText>()
    val errorChannel = _errorChannel.receiveAsFlow()
    var recipeItem by mutableStateOf<RecipesItem?>(null)
    var itemAlreadyExists = mutableStateOf(false)
    private val foodId = savedStateHandle.get<String>(Constants.FOOD_ID)!!

    init {
        viewModelScope.launch {
            checkIfItemsIsInCart()
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

    private suspend fun getRecipeDetails(id: String) {
        _state.value = RecipesDetailsViewState(isLoading = true)
        authRepository.getRestaurantInfoFromFirestore().onEach { response ->
            when (response) {
                is Resource.Success -> {
                    _state.value = RecipesDetailsViewState(isLoading = false)
                    recipeItem = response.data?.flatMap { it.food!! }?.map { it.recipesItem }
                        ?.first { it?.foodId == id }
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

    private suspend fun checkIfItemsIsInCart() {
        _state.value = RecipesDetailsViewState(isLoading = true)
        authRepository.getCartItemsFromFirestore().onEach { result ->
            when(result) {
                is Resource.Loading -> {
                    _state.value = RecipesDetailsViewState(isLoading = true)
                }
                is Resource.Success -> {
                    try {
                        val cartItems = result.data?.toList()!!.filter { it.userId == Firebase.auth.currentUser?.uid }
                        _state.value = RecipesDetailsViewState(isLoading = false)
                        itemAlreadyExists.value = cartItems.first { it.product_id.isNotEmpty() }.product_id == foodId.toString()
                        if (itemAlreadyExists.value) {
                            addToCart.value = R.string.view_in_cart
                        }
                        Log.i(TAG, "FIRESTORE RESPONSE: ${result.data}")
                        Log.i(TAG, "FIRESTORE RESPONSE FOOD ID: $foodId")
                        Log.i(TAG, "FIRESTORE RESPONSE ITEMALRADY IN CART: ${itemAlreadyExists.value}")
                    } catch (e: NoSuchElementException) { }
                }
                is Resource.Error -> {
                    _errorChannel.send(UiText.DynamicString(result.message.toString()))
                }
            }
        }.launchIn(viewModelScope)
    }

    fun onAddToCart(toCartScreen: () -> Unit) {
        _state.value = RecipesDetailsViewState(isLoading = true)
        if (addToCart.value == R.string.add_to_cart) {
            saveToFirebase(
                cartItem = CartItem(
                    product_owner_id = recipeItem?.restaurantId.toString(),
                    product_id = foodId.toString(),
                    title = recipeItem?.title.toString(),
                    unitPrice = recipeItem?.price.toString(),
                    cartPrice = recipeItem?.price.toString(),
                    image = recipeItem?.images?.first().toString(),
                    cartLimit = recipeItem?.cartLimit.toString(),
                    stock_quantity = recipeItem?.quantityInStock.toString(),
                    restaurant_id = recipeItem?.restaurantId.toString(),
                    userId = Firebase.auth.uid.toString()
                )
            ) {
                viewModelScope.launch {
                    _state.value = RecipesDetailsViewState(isLoading = false)
                    _errorChannel.send(UiText.StringResource(R.string.item_added))
                    addToCart.value = R.string.view_in_cart
                }
            }
        }
        else toCartScreen.invoke()
    }

    // SAVE DATA TO FIRESTORE CLOUD STORAGE
    private fun saveToFirebase(cartItem: CartItem, nextScreen: () -> Unit = {}) {
        val db = FirebaseFirestore.getInstance()
        val dbCollection = db.collection(Constants.DB_Collection_CartItems)

        if (cartItem.toString().isNotEmpty()) {
            dbCollection.add(cartItem)
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

    fun saveRecipe(recipesItem: RecipesItem) {
        viewModelScope.launch {
//            saveRecipeUseCase(recipesItem)
        }
    }
}
