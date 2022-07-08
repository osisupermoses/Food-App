package com.osisupermoses.food_ordering_app.ui.checkout

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.osisupermoses.food_ordering_app.common.Constants
import com.osisupermoses.food_ordering_app.common.Resource
import com.osisupermoses.food_ordering_app.data.pref.repository.DataStoreRepository
import com.osisupermoses.food_ordering_app.domain.model.Card
import com.osisupermoses.food_ordering_app.domain.repository.AuthRepository
import com.osisupermoses.food_ordering_app.util.UiText
import com.osisupermoses.food_ordering_app.util.paystack.CheckoutActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.lang.Math.abs
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val paystackCheckout: CheckoutActivity,
    private val authRepository: AuthRepository,
    private val dataStoreRepository: DataStoreRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val TAG = "CHECKOUTVIEWMODEL"

    var selectedCardHolderIndex by mutableStateOf<Int?>(null)
    var cardNumber by mutableStateOf(TextFieldValue())
    var address by mutableStateOf("")
    val state = mutableStateOf(CheckoutScreenState())

    var nameText by mutableStateOf(TextFieldValue())
    var expiryNumber by mutableStateOf(TextFieldValue())
    var cvcNumber by mutableStateOf(TextFieldValue())

    private val _errorChannel = Channel<UiText>()
    val errorChannel = _errorChannel.receiveAsFlow()

    val currencySymbol by mutableStateOf("₦")
    val itemPrice = savedStateHandle.get<String>(Constants.ITEM_PRICE)!!
    val deliveryFee = savedStateHandle.get<String>(Constants.DELIVERY_FEE)!!

    val discount = 0.00
    val total = itemPrice.toDoubleOrNull()!! + deliveryFee.toDoubleOrNull()!! + discount

    init {
        paystackCheckout.initializePaystack()
    }

    fun makePayment(
        cardNumber: String = "4084084084084081",
        cardExpiry: String = "07/23",
        cvv: String = "408",
        amount: String = total.toString(),
        customerEmail: String = "osisuper_moses@yahoo.com",
        context: Context,
        successScreen: () -> Unit = {},
        failedScreen: () -> Unit = {}
    ) {
        state.value = CheckoutScreenState(isLoading = true)
        performCharge(
            context = context,
            onSuccess = {
                state.value = CheckoutScreenState(
                    isLoading = false,
                    transReference = it
                )
                Log.i(TAG, "Response: ${state.value.transReference}")
                successScreen()
            },
            onFailed = {
                viewModelScope.launch {
                    state.value = CheckoutScreenState(
                        isLoading = false,
                        error = it.message ?: "Couldn't reach the server"
                    )
                    Log.i(TAG, "Response: ${state.value.error}")
                    _errorChannel.send(UiText.DynamicString(state.value.error.toString()))
                    failedScreen()
                }
            }
        )
    }

    private fun performCharge(
        cardNumber: String = "4084084084084081",
        cardExpiry: String = "07/23",
        cvv: String = "408",
        amount: String = total.toString(),
        customerEmail: String = "osisuper_moses@yahoo.com",
        context: Context,
        onSuccess: (String) -> Unit,
        onFailed: (Throwable) -> Unit
    ) {
        paystackCheckout.paystackGateway(
            cardNumber, cardExpiry, cvv, amount, customerEmail, context, onSuccess, onFailed
        )
    }

    private fun saveLoggedinInUserIntoFirestore(nextScreen: () -> Unit) {
        viewModelScope.launch {
            authRepository.getUserInfoFromFirestore().onEach { result ->
                when(result) {
                    is Resource.Loading -> {
                        state.value = CheckoutScreenState(isLoading = true)
                    }
                    is Resource.Success -> {
                        val currentUserId = result.data?.filter { user ->
                            user.userId == Firebase.auth.currentUser?.uid ||
                                    user.email == Firebase.auth.currentUser?.email
                        }

                        if (currentUserId.isNullOrEmpty()) {
                            saveToFirebase(cardInfo(), nextScreen)
                        } else {
                            nextScreen()
                        }
                        state.value = CheckoutScreenState(isLoading = false)
                    }
                    is Resource.Error -> {
                        _errorChannel.send(UiText.DynamicString(result.message.toString()))
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    fun onClickSaveCard() {
        val card: MutableList<Card> = mutableListOf(cardInfo())
        state.value = CheckoutScreenState(cardList = card)
        Log.i(TAG, "CARD LIST: ${state.value.cardList}")
        Log.i(TAG, "CARD INFO: ${cardInfo()}")
        Log.i(TAG, "CARD: $card")
        saveToFirebase(cardInfo())
    }

    private fun cardInfo(): Card {
        val random = abs((0..999999999999).random())
        return Card(
            id = random.toString().substring(0, 5).toInt(),
            cardHolder = nameText.text,
            cardLast4digits = cardNumber.text.takeLast(4),
            cardExpiry = expiryNumber.text,
            cardNumber = cardNumber.text,
            cardCvv = cvcNumber.text
        )
    }

    private fun saveToFirebase(card: Card, nextScreen: () -> Unit = {}) {
        val db = FirebaseFirestore.getInstance()
        val dbCollection = db.collection(Constants.DB_Collection)

        if (card.toString().isNotEmpty()) {
            dbCollection.add(card)
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
}