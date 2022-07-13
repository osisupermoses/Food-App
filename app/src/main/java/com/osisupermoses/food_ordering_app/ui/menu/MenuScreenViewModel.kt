package com.osisupermoses.food_ordering_app.ui.menu

import android.app.Activity
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.osisupermoses.food_ordering_app.common.Resource
import com.osisupermoses.food_ordering_app.data.pref.repository.DataStoreRepository
import com.osisupermoses.food_ordering_app.domain.model.Food
import com.osisupermoses.food_ordering_app.domain.model.Restaurant
import com.osisupermoses.food_ordering_app.domain.repository.AuthRepository
import com.osisupermoses.food_ordering_app.domain.repository.FoodOrderingRepository
import com.osisupermoses.food_ordering_app.util.UiText
import com.osisupermoses.food_ordering_app.util.getPath
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import java.io.*
import javax.inject.Inject


@HiltViewModel
class MenuScreenViewModel @Inject constructor(
    private val authRepo: AuthRepository,
    private val dataStoreRepository: DataStoreRepository,
    private val repository: FoodOrderingRepository
) : ViewModel() {

    val TAG = "MENUVIEWMODEL"

    val state = mutableStateOf(MenuState())

    private val _errorChannel = Channel<UiText>()
    val errorChannel = _errorChannel.receiveAsFlow()

    var isAdmin by mutableStateOf(false)
    var accountName by mutableStateOf("")
    var restaurantList: List<Restaurant> = mutableListOf()
    var foods: List<Food> = mutableListOf()
    var userIsDeleted by mutableStateOf(false)


    val auth = Firebase.auth

    val topClickIndex = mutableStateOf(0)

    init {
        getMenuScreenItems()
    }

    fun getPopularImageUri(food: Food): String? {
        return dataStoreRepository.getPopularImageUri(food)
    }

    fun getRestaurantFrontalImageUri(restaurant: Restaurant): String? {
        return dataStoreRepository.getRestaurantFrontalImageUri(restaurant)
    }

    private fun getMenuScreenItems() {
        viewModelScope.launch {
            state.value = MenuState(isLoading = true)

            // FIRESTORE DATA
            val getFirestoreUsers = async { authRepo.getUserInfoFromFirestore() }
            val getFirestoreRestaurants = async { authRepo.getRestaurantInfoFromFirestore() }

            // FAKE REPOSITORY DATA
            val getFoods = async { repository.getFoodList() }
            val getRestaurants = async { repository.getRestaurantList() }

            // CHECKS WHETHER USER HAS ADMIN RIGHTS OR NOT
            getFirestoreUsers.await().onEach { response ->
                when (response) {
                    is Resource.Success -> {
                        state.value = MenuState(isLoading = false)
                        val user = response.data?.toList()
                        val currentUser = user?.first { it.userId == auth.currentUser?.uid }
                        isAdmin = currentUser?.hasAdminRights == "01234"
                        accountName = currentUser!!.fullName.split(" ").first()

                        Log.i(TAG, "FIREBASE USERS: $user")
                        Log.i(TAG, "FIREBASE RESPONSE: $isAdmin")
                        Log.i(TAG, "FIREBASE USERNAME: $accountName")
                    }
                    is Resource.Error -> {
                        state.value = MenuState(
                            isLoading = false,
                            error = response.message.toString(),
                        )
                    }
                    else -> Unit
                }
            }.launchIn(viewModelScope)

            // GETTING LIST OF RESTAURANTS FROM FIREBASE
            getFirestoreRestaurants.await().onEach { response ->
                when(response) {
                    is Resource.Success -> {
                        state.value = MenuState(isLoading = false)
                        val savedData = response.data?.toList()
//                        val currentUserRestaurants = user?.filter { it.userId == auth.currentUser?.uid }
                        if (savedData != null) {
                            restaurantList = savedData
                            foods = savedData.flatMap { it.food!! }

                            Log.i(TAG, "FIREBASE RESTAURANT LIST: $restaurantList")
                            Log.i(TAG, "FIREBASE FOODS: $foods")
                        }
                    }
                    is Resource.Error -> {
                        state.value = MenuState(
                            isLoading = false,
                            error = response.message ?: "Unknown Error"
                        )
                        Log.i(TAG, "RESTAURANTS: ${response.message!!}")
                        _errorChannel.send(UiText.DynamicString(response.message.toString()))
                    }
                    else -> Unit
                }
            }.launchIn(this)
        }
    }

    // SIGN USER OUT OF FIREBASE
    fun signOut(context: Context, nextScreen: () -> Unit) {
        state.value = MenuState(isLoading = true)
        AuthUI.getInstance().signOut(context).addOnCompleteListener {
            state.value = MenuState(isLoading = false)
            nextScreen.invoke()
        }
    }

    // DELETE USER ACCOUNT FROM FIRESTORE
    fun deleteAccount(nextScreen: () -> Unit) {
        viewModelScope.launch {
            state.value = MenuState(isLoading = true)
            authRepo.deleteUserAccount().collect { result ->
                when(result) {
                    is Resource.Loading -> {
                        state.value = MenuState(isLoading = true)
                    }
                    is Resource.Success -> {
                        state.value = MenuState(isLoading = false)
                        userIsDeleted = result.data == true
                        if (userIsDeleted) {
                            nextScreen()
                        }
                    }
                    is Resource.Error -> {
                        state.value = MenuState(
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
}