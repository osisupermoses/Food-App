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
import com.osisupermoses.food_ordering_app.R
import com.osisupermoses.food_ordering_app.common.Constants
import com.osisupermoses.food_ordering_app.common.Resource
import com.osisupermoses.food_ordering_app.data.pref.PreferencesKeys
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
    var address by mutableStateOf(TextFieldValue())

    var saveBtnVisibility by mutableStateOf(true)
    var enabled by mutableStateOf(true)
    var readOnly by mutableStateOf(false)
    var topText by mutableStateOf(R.string.input_a_valid_address_below)
    var successDialogIsVisible by mutableStateOf(false)
    var errorDialogIsVisible by mutableStateOf(false)

    private val firebaseAuth = Firebase.auth

    var cardHolderName by mutableStateOf(TextFieldValue())
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

    // SEND CARD DETAILS TO PAYSTACK AND RETURNS RESPONSES IN FORM OF STRING
    fun makePayment(
        cardNumber: String = "4084084084084081",
        cardExpiry: String = "07/23",
        cvv: String = "408",
        amount: String = total.toString(),
        customerEmail: String? = firebaseAuth.currentUser?.email,
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
            when {
                expiryNumber.text.trim().count() < 4 -> {
                    state.value = CheckoutScreenState(isLoading = false)
                    _errorChannel.send(
                        UiText.StringResource(
                            R.string.please_enter_a_valid_expiry_date
                        )
                    )
                }
                cvcNumber.text.trim().count() < 3 -> {
                    state.value = CheckoutScreenState(isLoading = false)
                    _errorChannel.send(
                        UiText.StringResource(
                            R.string.please_enter_a_valid_cvv_date
                        )
                    )
                }
                cardNumber.text.trim().count() < 16 -> {
                    state.value = CheckoutScreenState(isLoading = false)
                    _errorChannel.send(
                        UiText.StringResource(
                            R.string.please_enter_a_valid_card_num
                        )
                    )
                }
                cardNumber.text.toList().any { it.isLetter() } ||
                        expiryNumber.text.toList().any { it.isLetter() } ||
                cvcNumber.text.toList().any { it.isLetter() } -> {
                    state.value = CheckoutScreenState(isLoading = false)
                    _errorChannel.send(
                        UiText.StringResource(
                            R.string.card_number_expiry_number_cvv_cannot
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
                cardHolder = cardHolderName.text,
                userId = it.uid,
                cardNumber = cardNumber.text,
                cardExpiry = expiryNumber.text,
                cardCvv = cvcNumber.text,
            )
        }
    }

    // SAVE DATA TO FIRESTORE CLOUD STORAGE
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

    //CRUD - UPDATE CARD DETAIL(S)
    private fun updateDbWithId(card: Card, nextScreen: () -> Unit) {
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

    //CRUD = DELETE CARD ITEM
    fun onDeleteCard(cardId: String) {
        state.value = CheckoutScreenState(isLoading = true)
        FirebaseFirestore.getInstance()
            .collection(Constants.DB_Collection_Cards)
            .document(cardId)
            .delete()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    state.value = CheckoutScreenState(isLoading = false)
                    viewModelScope.launch {
                        _errorChannel.send(
                            UiText.StringResource(R.string.card_was_successfully_deleted)
                        )
                    }
                }
            }
            .addOnFailureListener {
                state.value = CheckoutScreenState(isLoading = false)
                viewModelScope.launch {
                    Log.e("Error", "Error deleting document", it)
                    _errorChannel.send(UiText.StringResource(R.string.couldnot_delete_card))
                }
            }
    }

    // GET SAVED ADDRESS FROM DATASTORE(i.e just like it SharedPref)
    private fun getAddress() {
        viewModelScope.launch {
            dataStoreRepository.read(PreferencesKeys.addressKey).onEach { response ->
                address = TextFieldValue(response)
                Log.i(TAG, "SAVED ADDRESS: $response")
            }
        }
    }

    // SAVE ADDRESS TO DATASTORE FOR PERSISTENCE
    fun onSaveAddressClick(value: TextFieldValue) {
        viewModelScope.launch {
            if (value.text.isNotBlank()) {
                dataStoreRepository.save(PreferencesKeys.addressKey, value.text)
                Log.i(TAG, "ADDRESS: $value")
                _errorChannel.send(UiText.StringResource(R.string.address_saved))
                saveBtnVisibility = false
                readOnly = true
                enabled = false
                address = value
            } else _errorChannel.send(UiText.StringResource(R.string.please_input_an_address))
        }
    }

    // EDIT ADDRESS AND SAVE AGAIN
    fun onEditAddressClick(value: TextFieldValue) {
        if (value.text.isNotBlank()) {
            topText = R.string.you_can_edit
            readOnly = false
            enabled = true
            address = value
            saveBtnVisibility = true
        }
    }

    // WHEN SET CARD DETAILS BASED ON THE SELECTED CARD IF THERE IS MORE THAN ONE
    fun onPickCard(card: Card) {
        cardHolderName = TextFieldValue(text = card.cardHolder)
        cvcNumber = TextFieldValue(text = card.cardCvv)
        expiryNumber = TextFieldValue(text = card.cardExpiry)
        cardNumber = TextFieldValue(text = card.cardNumber)
    }
}