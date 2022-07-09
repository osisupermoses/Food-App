package com.osisupermoses.food_ordering_app.ui.checkout

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.osisupermoses.food_ordering_app.R
import com.osisupermoses.food_ordering_app.common.Constants
import com.osisupermoses.food_ordering_app.common.Resource
import com.osisupermoses.food_ordering_app.data.pref.PreferencesKeys
import com.osisupermoses.food_ordering_app.data.pref.repository.DataStoreRepository
import com.osisupermoses.food_ordering_app.domain.model.Card
import com.osisupermoses.food_ordering_app.domain.repository.AuthRepository
import com.osisupermoses.food_ordering_app.util.UiText
import com.osisupermoses.food_ordering_app.util.paystack.CheckoutActivity
import com.osisupermoses.food_ordering_app.util.toasty
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.lang.Math.abs
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val paystackCheckout: CheckoutActivity,
    private val authRepository: AuthRepository,
    private val dataStoreRepository: DataStoreRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val TAG = "CHECKOUTVIEWMODEL"

    val state = mutableStateOf(CheckoutScreenState())
    var selectedCardHolderIndex by mutableStateOf<Int?>(null)
    var cardNumber by mutableStateOf(TextFieldValue())
    var address by mutableStateOf("")

    var saveBtnVisibility by mutableStateOf(true)
    var enabled by mutableStateOf(true)
    var readOnly by mutableStateOf(false)
    var topText by mutableStateOf(R.string.input_a_valid_address_below)
    var successDialogIsVisible by mutableStateOf(false)
    var errorDialogIsVisible by mutableStateOf(false)

    private val firebaseAuth = Firebase.auth

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
        getCardsFromFirestore()
        getAddress()
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

    private fun getCardsFromFirestore(nextScreen: () -> Unit = {}) {
        viewModelScope.launch {
            authRepository.getCardInfoFromFirestore().onEach { result ->
                when(result) {
                    is Resource.Loading -> {
                        state.value = CheckoutScreenState(isLoading = true)
                    }
                    is Resource.Success -> {
                        val cards = result.data?.toList()
                        state.value = CheckoutScreenState(
                            isLoading = false,
                            cardList = cards
                        )
                        Log.i(TAG, "FIREBASE RESPONSE: ${result.data}")
                        nextScreen.invoke()
                    }
                    is Resource.Error -> {
                        state.value = CheckoutScreenState(
                            isLoading = false,
                            error = result.message ?: "Something went wrong"
                        )
                        _errorChannel.send(UiText.DynamicString(result.message.toString()))
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    fun onClickSaveCard(nextScreen: () -> Unit) {
        viewModelScope.launch {
            state.value = CheckoutScreenState(isLoading = true)
            val card: MutableList<Card> = mutableListOf(cardInfo()!!)
            state.value = CheckoutScreenState(cardList = card)
            Log.i(TAG, "CARD LIST: ${state.value.cardList}")
            Log.i(TAG, "CARD INFO: ${cardInfo()}")
            Log.i(TAG, "CARD: $card")
            when {
                expiryNumber.text.count() < 4 -> {
                    state.value = CheckoutScreenState(isLoading = false)
                    _errorChannel.send(
                        UiText.StringResource(
                            R.string.please_enter_a_valid_expiry_date
                        )
                    )
                }
                cvcNumber.text.count() < 3 -> {
                    state.value = CheckoutScreenState(isLoading = false)
                    _errorChannel.send(
                        UiText.StringResource(
                            R.string.please_enter_a_valid_cvv_date
                        )
                    )
                }
                cardNumber.text.count() < 16 -> {
                    state.value = CheckoutScreenState(isLoading = false)
                    _errorChannel.send(
                        UiText.StringResource(
                            R.string.please_enter_a_valid_card_num
                        )
                    )
                }
                else -> cardInfo()?.let {
                    saveToFirebase(
                        it,
                        nextScreen = {
                            getCardsFromFirestore(nextScreen)
                        }
                    )
                }
            }
        }
    }

    private fun cardInfo(): Card? {
        return firebaseAuth.currentUser?.let {
            Card(
                cardHolder = nameText.text,
                userId = it.uid,
                cardLast4digits = cardNumber.text.takeLast(4),
                cardExpiry = expiryNumber.text,
                cardCvv = cvcNumber.text
            )
        }
    }

    private fun saveToFirebase(card: Card, nextScreen: () -> Unit = {}) {
        val db = FirebaseFirestore.getInstance()
        val dbCollection = db.collection(Constants.DB_Collection_Cards)

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

    private fun updateDbWithId(card: Card, nextScreen: () -> Unit) {
        if (cardNumber.text.isNotEmpty()) {
            val cardId = card.id.toString()
            FirebaseFirestore.getInstance()
                .collection(Constants.DB_Collection_Cards)
                .document(cardId)
                .update(hashMapOf("id" to cardId) as Map<String, Any>)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        nextScreen()
                    }
                }
                .addOnFailureListener {
                    Log.w("Error", "Error updating document", it)
                }
        }
    }

    private fun getAddress() {
        viewModelScope.launch {
            dataStoreRepository.read(PreferencesKeys.addressKey).onEach { response ->
                when(response) {
                    is Resource.Success -> address = response.data.toString()
                    else -> {
                        Log.e(TAG, "ERROR SAVING ADDRESS: ${response.message}")
                        state.value = CheckoutScreenState(error = response.message)
                        _errorChannel.send(UiText.DynamicString(response.message.toString()))
                    }
                }
            }
        }
    }

    fun onSaveAddressClick(value: String) {
        viewModelScope.launch {
            if (value.isNotBlank()) {
                dataStoreRepository.save(PreferencesKeys.addressKey, address)
                _errorChannel.send(UiText.StringResource(R.string.address_saved))
                saveBtnVisibility = false
                readOnly = true
                enabled = false
                address = value
            } else _errorChannel.send(UiText.StringResource(R.string.please_input_an_address))
        }
    }

    fun onEditAddressClick(value: String) {
        if (value.isNotBlank()) {
            topText = R.string.you_can_edit
            readOnly = false
            enabled = true
            address = value
            saveBtnVisibility = true
        }
    }
}