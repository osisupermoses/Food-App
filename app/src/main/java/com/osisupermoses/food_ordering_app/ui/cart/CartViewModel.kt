package com.osisupermoses.food_ordering_app.ui.cart

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.osisupermoses.food_ordering_app.R
import com.osisupermoses.food_ordering_app.common.Constants
import com.osisupermoses.food_ordering_app.common.Resource
import com.osisupermoses.food_ordering_app.data.pref.repository.DataStoreRepository
import com.osisupermoses.food_ordering_app.domain.model.CartItem
import com.osisupermoses.food_ordering_app.domain.repository.AuthRepository
import com.osisupermoses.food_ordering_app.ui.cart.components.CartScreenState
import com.osisupermoses.food_ordering_app.util.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    val TAG = "CARTVIEWMODEL"
    var state by mutableStateOf(CartScreenState())

    val firebaseAuth = Firebase.auth
    private val _errorChannel = Channel<UiText>()
    val errorChannel = _errorChannel.receiveAsFlow()

    var cartItemList: List<CartItem>? = listOf()

    var newPrice by mutableStateOf("")
    var selectedIndex by mutableStateOf<Int?>(null)
    var cartQuantity by mutableStateOf(1)
    var deliveryCharges by mutableStateOf("200")

    init {
        getCartItems()
    }

    private fun getCartItems() {
        state = CartScreenState(isLoading = true)
        viewModelScope.launch {
            authRepository.getCartItemsFromFirestore().onEach { result ->
                when(result) {
                    is Resource.Loading -> {
                        state = CartScreenState(isLoading = true)
                    }
                    is Resource.Success -> {
                        val cartItems = result.data?.toList()!!.filter { it.userId == firebaseAuth.currentUser?.uid }
                        state = CartScreenState(isLoading = false)
                        cartItemList = cartItems
                        Log.i(TAG, "FIREBASE RESPONSE: ${result.data}")
                    }
                    is Resource.Error -> {
                        state = CartScreenState(
                            isLoading = false,
                            error = result.message ?: "Something went wrong"
                        )
                        _errorChannel.send(UiText.DynamicString(result.message.toString()))
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    fun getCartItemImageUriToString(uri: Uri): String? {
        return dataStoreRepository.getImageUri(uri)
    }

    fun onRemoveQuantityClick(cartItem: CartItem) {
        if (cartItem.cart_quantity.toInt() > 1) {
            state = CartScreenState(isLoading = true)
            cartQuantity = cartItem.cart_quantity.toInt() - 1
            newPrice = (cartItem.unitPrice.toDouble() * cartQuantity).toString()
            updateDbWithId(
                cartItem.id.toString(),
                hashMapOf(
                    "cartPrice" to newPrice,
                    "cart_quantity" to cartQuantity.toString()
                ).toMap()
            ) {
                getCartItems()
            }
        } else Unit
    }

    fun subTotalAmount(): String {
        val total = cartItemList?.filter { it.cartPrice.isNotBlank() }?.sumOf { it.cartPrice.toDouble() }
        return total.toString()
    }

    fun totalAmount(): String {
        return (subTotalAmount().toDouble() + deliveryCharges.toDouble()).toString()
    }

    fun onAddQuantityClick(cartItem: CartItem) {
        if (cartItem.cart_quantity.toInt() < cartItem.cartLimit.toInt() &&
            cartItem.cart_quantity.toInt() < cartItem.stock_quantity.toInt()
        ) {
            state = CartScreenState(isLoading = true)
            cartQuantity = cartItem.cart_quantity.toInt() + 1
            newPrice = (cartItem.unitPrice.toDouble() * cartQuantity).toString()
            updateDbWithId(
                cartItem.id.toString(),
                hashMapOf(
                    "cartPrice" to newPrice,
                    "cart_quantity" to cartQuantity.toString()
                ).toMap()
            ) {
                getCartItems()
            }
        } else {
            viewModelScope.launch {
                _errorChannel.send(UiText.StringResource(R.string.you_have_reached_cart_or_stock_limit))
            }
        }
    }
    
    fun onDeleteClick(cartItemId: String) {
        state = CartScreenState(isLoading = true)
        FirebaseFirestore.getInstance()
            .collection(Constants.DB_Collection_CartItems)
            .document(cartItemId)
            .delete()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    state = CartScreenState(isLoading = false)
                    viewModelScope.launch {
                        getCartItems()
                        _errorChannel.send(
                            UiText.StringResource(R.string.card_was_successfully_deleted)
                        )
                    }
                }
            }
            .addOnFailureListener {
                state = CartScreenState(isLoading = false)
                viewModelScope.launch {
                    Log.e("Error", "Error deleting document", it)
                    _errorChannel.send(UiText.StringResource(R.string.could_not_delete_card))
                }
            }
    }

    fun onItemClick(index: Int, toItemDetail: () -> Unit) {
        selectedIndex = index
        toItemDetail.invoke()
    }

    //CRUD - UPDATE CARD DETAIL(S)
    private fun updateDbWithId(
        cardId: String,
        updateItems: Map<String, Any>,
        nextScreen: () -> Unit
    ) {
        FirebaseFirestore.getInstance()
            .collection(Constants.DB_Collection_CartItems)
            .document(cardId)
            .update(updateItems)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    state = CartScreenState(isLoading = false)
                    nextScreen()
                }
            }
            .addOnFailureListener {
                state = CartScreenState(isLoading = false)
                Log.e(TAG, "FIRESTORE UPDATE ERROR: ${it.message!!}")
                viewModelScope.launch {
                    _errorChannel.send(UiText.DynamicString(it.message.toString()))
                }
            }
    }
}