package com.osisupermoses.food_ordering_app.ui.list_item

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.osisupermoses.food_ordering_app.R
import com.osisupermoses.food_ordering_app.common.Constants
import com.osisupermoses.food_ordering_app.domain.model.Food
import com.osisupermoses.food_ordering_app.domain.model.RecipesItem
import com.osisupermoses.food_ordering_app.domain.model.Restaurant
import com.osisupermoses.food_ordering_app.domain.repository.AuthRepository
import com.osisupermoses.food_ordering_app.util.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListItemViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {


    val TAG = "LISTITEMVIEWMODEL"
    var firebaseAuth = Firebase.auth

    var successDialogIsVisible by mutableStateOf(false)

    private var _listOfSelectedImages = mutableStateListOf<Uri>()
    var listOfSelectedImages: List<Uri> = _listOfSelectedImages

    var state by mutableStateOf(ListItemScreenState())
        private set

    private val _errorChannel = Channel<UiText>()
    val errorChannel = _errorChannel.receiveAsFlow()

    var businessName by mutableStateOf("")
    var productTitle by mutableStateOf("")
    var productPrice by mutableStateOf("")
    var productDescription by mutableStateOf("")
    var productQuantity by mutableStateOf("")

    fun updateSelectedImageList(listOfImages: List<Uri>) {
        val updatedImageList = _listOfSelectedImages.toMutableList()
        viewModelScope.launch {
            updatedImageList += listOfImages
                _listOfSelectedImages.addAll(updatedImageList.distinct())
        }
    }

    fun onItemRemove(index: Int) {
        val updatedImageList = _listOfSelectedImages
        viewModelScope.launch {
            updatedImageList.removeAt(index)
                _listOfSelectedImages.addAll(updatedImageList.distinct())
        }
    }

    fun onSubmitListingClick(nextScreen: () -> Unit) {
        viewModelScope.launch {
            state = ListItemScreenState(isLoading = true)
            if (
                _listOfSelectedImages.isNotEmpty() &&
                businessName.isNotBlank() &&
                productDescription.isNotBlank() &&
                productPrice.isNotBlank() &&
                productTitle.isNotBlank() &&
                productQuantity.isNotBlank()
            ) {
                saveToFirestore(restaurantItem(), nextScreen)
            } else if (_listOfSelectedImages.isEmpty()) {
                _errorChannel.send(UiText.StringResource(R.string.please_choose_images))
            } else {
                _errorChannel.send(UiText.StringResource(R.string.you_have_to_fill_all_fields))
            }
            Log.i(TAG, "LIST OF URI: ${_listOfSelectedImages}")
        }
    }

    private val randomLongFood = (0..99999999999).random()
    private val randomLongRecipe = (0..99999999999).random()
    private val randomLongRestaurant = (0..99999999999).random()

    private fun restaurantItem() = Restaurant(
        restaurantId = randomLongRestaurant,
        frontalImage =
            if (_listOfSelectedImages.size > 1) _listOfSelectedImages.last().toString()
            else _listOfSelectedImages.first().toString(),
        restaurantName = businessName,
        restaurantDescription = "Good Enough",
        minOrderPrice = 1000.00,
        dateAdded = System.currentTimeMillis(),
        food = listOf(
            Food(
                id = randomLongFood,
                name = productTitle,
                price = productPrice.replace(",", "").toDouble(),
                image = _listOfSelectedImages.first().toString(),
                orderDescription = productDescription,
                foodCategory = "African food",
                orderRating = 0.00,
                recipesItem = RecipesItem(
                    id = randomLongRecipe,
                    foodId = randomLongFood,
                    sustainable = true,
                    glutenFree = true,
                    veryPopular = true,
                    healthScore = 9.00,
                    title = productTitle,
                    aggregateLikes = 50,
                    price = productPrice.replace(",", "").toDouble(),
                    deliveryFee = 250.00,
                    creditsText = "Awesome",
                    readyInMinutes = 20,
                    dairyFree = true,
                    vegetarian = true,
                    images = _listOfSelectedImages.map { it.toString() },
                    veryHealthy = true,
                    vegan = false,
                    cheap = true,
                    spoonacularScore = 20.00,
                    sourceName = "Pizza",
                    percentCarbs = 3.11,
                    percentProtein = 4.22,
                    percentFat = 6.90,
                    nutrientsAmount = 8.00,
                    servings = 20,
                    step = listOf("Unavailable at the moment"),
                    ingredientOriginalString = listOf("Flour", "Pasta", "Jam", "All hard-coded"),
                    saved = true,
                    userId = firebaseAuth.currentUser?.uid!!
                ),
                userId = firebaseAuth.currentUser?.uid!!
            )
        ),
        userId = firebaseAuth.currentUser?.uid!!
    )

    // SAVE RESTAURANT DATA TO FIRESTORE CLOUD STORAGE
    private fun saveToFirestore(restaurant: Restaurant, nextScreen: () -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val dbCollection = db.collection(Constants.DB_Collection_Restaurants)

        dbCollection.add(restaurant)
            .addOnSuccessListener { documentRef ->
                val docId = documentRef.id
                dbCollection.document(docId)
                    .update(hashMapOf("id" to docId) as Map<String, Any>)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            state = ListItemScreenState(isLoading = false)
                            nextScreen()
                        }
                    }.addOnFailureListener {
                        viewModelScope.launch {
                            state = ListItemScreenState(error = it.message!!)
                            _errorChannel.send(UiText.DynamicString(it.message.toString()))
                            Log.w("Error", "SaveToFirebase: Error updating doc", it)
                        }
                    }
            }
    }
}