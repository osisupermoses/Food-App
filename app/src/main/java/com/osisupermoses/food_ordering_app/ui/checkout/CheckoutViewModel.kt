package com.osisupermoses.food_ordering_app.ui.checkout

import android.content.Context
import android.os.Parcelable
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
import com.osisupermoses.food_ordering_app.data.pref.repository.DataStoreRepository
import com.osisupermoses.food_ordering_app.domain.model.Address
import com.osisupermoses.food_ordering_app.domain.model.Card
import com.osisupermoses.food_ordering_app.domain.model.CardIds
import com.osisupermoses.food_ordering_app.domain.repository.AuthRepository
import com.osisupermoses.food_ordering_app.util.UiText
import com.osisupermoses.food_ordering_app.util.paystack.CheckoutActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.Serializable
import java.util.*
import javax.inject.Inject
import kotlin.NoSuchElementException

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
    var cardNum by mutableStateOf(TextFieldValue())
    var address by mutableStateOf(TextFieldValue())

    var saveBtnVisibility by mutableStateOf(false)
    var enabled by mutableStateOf(false)
    var readOnly by mutableStateOf(true)
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
    private val isFromCart = savedStateHandle.get<Boolean>(Constants.IS_FROM_CART)!!
    private val cartIds = savedStateHandle.get<String>(Constants.CART_ITEMS_IDS)!!
    var addressList: List<Address> = listOf()
    var cardList: List<Card> = listOf()
    var transactionReference by mutableStateOf("")

    val discount = 0.00
    val total = itemPrice.toDoubleOrNull()!! + deliveryFee.toDoubleOrNull()!! + discount

    init {
        paystackCheckout.initializePaystack()
        getAddress()
        getCardsFromFirestore()
        Log.i(TAG, "CART ITEMS ID: $cartIds")
    }

    // SEND CARD DETAILS TO PAYSTACK AND RETURNS RESPONSES IN FORM OF STRING
    fun onCheckoutClick(
        cardNumber: String = cardNum.text,
        cardExpiry: String = expiryNumber.text,
        cvv: String = cvcNumber.text,
        amount: String = total.toString(),
        customerEmail: String? = firebaseAuth.currentUser?.email,
        context: Context,
        successScreen: () -> Unit = {},
        failedScreen: () -> Unit = {}
    ) {
        errorDialogIsVisible = false
        state.value = CheckoutScreenState(isLoading = true)
        performCharge(
            cardNumber = cardNumber,
            cardExpiry = cardExpiry,
            cvv = cvv,
            amount = amount,
            customerEmail = customerEmail ?: "",
            context = context,
            onSuccess = {
                state.value = CheckoutScreenState(isLoading = false)
                transactionReference = it
                Log.i(TAG, "Response: $transactionReference")
                Log.i(TAG, "CARD NUM: $cardNumber, EXPIRY: $cardExpiry, CVV: $cvv, " +
                        "AMOUNT: $amount, CUSTOMEREMAIL: $customerEmail"
                )
                if (isFromCart) {
                    clearCartItems {
                        successScreen.invoke()
                    }
                } else successScreen()
            },
            onFailed = {
                viewModelScope.launch {
                    state.value = CheckoutScreenState(
                        isLoading = false,
                        error = it.message ?: "Couldn't reach the server"
                    )
                    Log.i(TAG, "Response: ${state.value.error}")
                    Log.i(TAG, "CARD NUM: $cardNumber, EXPIRY: $cardExpiry, CVV: $cvv, " +
                            "AMOUNT: $amount, CUSTOMEREMAIL: $customerEmail"
                    )
                    failedScreen()
                }
            }
        )
    }

    // PAYSTACK PAYMENT GATEWAY REFACTORED
    private fun performCharge(
        cardNumber: String,
        cardExpiry: String,
        cvv: String,
        amount: String,
        customerEmail: String,
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
                        val cards = result.data?.toList()!!.filter { it.userId == firebaseAuth.currentUser?.uid }
                        state.value = CheckoutScreenState(isLoading = false)
                        cardList = cards
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

    // FORMATS INPUTS AND SAVE
    fun onClickSaveCard(nextScreen: () -> Unit) {
        viewModelScope.launch {
            val c: Calendar = Calendar.getInstance()
            val year: Int = c.get(Calendar.YEAR) % 100
            val month: Int = c.get(Calendar.MONTH)+1

            val card: MutableList<Card> = mutableListOf(cardInfo()!!)
            cardList = card
            Log.i(TAG, "CARD LIST: $cardList")
            Log.i(TAG, "CARD INFO: ${cardInfo()}")

            when {
                cardHolderName.text.isBlank() -> {
                    _errorChannel.send(
                        UiText.StringResource(
                            R.string.card_holder_field_cannot_be_blank
                        )
                    )
                }
                cardNum.text.trim().count() < 16 -> {
                    _errorChannel.send(
                        UiText.StringResource(
                            R.string.please_enter_a_valid_card_num
                        )
                    )
                }
                expiryNumber.text.isBlank() -> {
                    _errorChannel.send(UiText.StringResource(R.string.expiry_number_cannot_be_blank))
                }
                expiryNumber.text.isNotEmpty() &&
                        expiryNumber.text.substring(2).toInt() <= year &&
                        expiryNumber.text.substring(0, 2).toInt() < month ||
                        expiryNumber.text.substring(0, 2).toInt() > 12
                -> {
                    _errorChannel.send(UiText.StringResource(R.string.please_enter_a_valid_expiry_date))

                }
                cvcNumber.text.trim().count() < 3 -> {
                    _errorChannel.send(
                        UiText.StringResource(
                            R.string.please_enter_a_valid_cvv_date
                        )
                    )
                }
                cardNum.text.toList().any { !it.isDigit() } ||
                        expiryNumber.text.toList().any { !it.isDigit() } ||
                cvcNumber.text.toList().any { !it.isDigit() } -> {
                    _errorChannel.send(
                        UiText.StringResource(
                            R.string.card_number_expiry_number_cvv_cannot
                        )
                    )
                }
                else -> {
                    state.value = CheckoutScreenState(isLoading = true)
                    cardInfo()?.let {
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
    }

    // CARD ITEM
    private fun cardInfo(): Card? {
        return firebaseAuth.currentUser?.let {
            Card(
                cardHolder = cardHolderName.text,
                userId = it.uid,
                cardNumber = cardNum.text,
                cardExpiry = expiryNumber.text,
                cardCvv = cvcNumber.text,
            )
        }
    }

    // SAVE CARD DATA TO FIRESTORE CLOUD STORAGE
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

    //D OF CRUD = DELETE CARD ITEM
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
                        getCardsFromFirestore()
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
                    _errorChannel.send(UiText.StringResource(R.string.could_not_delete_card))
                }
            }
    }

    // THIS CLEARS ALL ITEMS CHECKED OUT THROUGH CART
    private fun clearCartItems(nextScreen: () -> Unit) {
        val decodedJson  = Json.decodeFromString<CardIds>(cartIds)
        Log.i(TAG, "DECODED CARTIDS: ${decodedJson.cardIds}")
        for (id in decodedJson.cardIds) {
            FirebaseFirestore.getInstance()
                .collection(Constants.DB_Collection_CartItems)
                .document(id)
                .delete()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        state.value = CheckoutScreenState(isLoading = false)
                        nextScreen.invoke()
                    }
                }
                .addOnFailureListener {
                    Log.i(TAG, it.message!!)
                }
        }
    }

    // GET SAVED ADDRESS FROM DATASTORE(i.e just like it SharedPref)
    private fun getAddress() {
        state.value = CheckoutScreenState(isLoading = true)
        viewModelScope.launch {
            authRepository.getAddressesFromFirestore().onEach { result ->
                when(result) {
                    is Resource.Loading -> {
                        state.value = CheckoutScreenState(isLoading = true)
                    }
                    is Resource.Success -> {
                        try {
                            val addresses = result.data?.toList()!!.filter { it.userId == firebaseAuth.currentUser?.uid }
                            state.value = CheckoutScreenState(isLoading = false)
                            addressList = addresses
                            address = TextFieldValue(text = addressList.last { it.address.isNotEmpty() }.address)
                            Log.i(TAG, "FIREBASE RESPONSE ADDRESSES: ${result.data}")
                        } catch (e: NoSuchElementException) { }
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

    // SAVE ADDRESS TO DATASTORE FOR PERSISTENCE
    fun onSaveAddressClick(value: TextFieldValue) {
        viewModelScope.launch {
            if (value.text.isNotBlank()) {
                if (!addressList.toList().isNullOrEmpty()) {
                    if (addressList.any { it.address == address.text.trim() }) {
                        Log.i(TAG, "ADDRESSLIST: $addressList")
                    } else {
                        state.value = CheckoutScreenState(isLoading = true)
                        saveAddressToFirebase(
                            Address(
                                address = address.text.trim(),
                                userId = firebaseAuth.uid.toString()
                            )
                        )
                        Log.i(TAG, "ADDRESS: $value")
                        _errorChannel.send(UiText.StringResource(R.string.address_saved))
                        getAddress()
                    }
                } else {
                    state.value = CheckoutScreenState(isLoading = true)
                    saveAddressToFirebase(
                        Address(
                            address = address.text.trim(),
                            userId = firebaseAuth.uid.toString()
                        )
                    )
                    Log.i(TAG, "ADDRESS: $value")
                    _errorChannel.send(UiText.StringResource(R.string.address_saved))
                    getAddress()
                }
                saveBtnVisibility = false
                readOnly = true
                enabled = false
                address = value
            } else _errorChannel.send(UiText.StringResource(R.string.please_input_an_address))
        }
    }

    // EDIT ADDRESS AND SAVE AGAIN
    fun onEditAddressClick(value: TextFieldValue) {
        topText = R.string.you_can_edit
        readOnly = false
        enabled = true
        address = value
        saveBtnVisibility = true
    }

    // WHEN SET CARD DETAILS BASED ON THE SELECTED CARD IF THERE IS MORE THAN ONE
    fun onPickCard(card: Card) {
        cardHolderName = TextFieldValue(text = card.cardHolder)
        cvcNumber = TextFieldValue(text = card.cardCvv)
        expiryNumber = TextFieldValue(text = card.cardExpiry)
        cardNum = TextFieldValue(text = card.cardNumber)
    }

    // CLEANS PREVIOUSLY ENTERED CARD FOR A NEW ONE
    fun onAddNewCard() {
        cardHolderName = TextFieldValue(text = "")
        cvcNumber = TextFieldValue(text = "")
        expiryNumber = TextFieldValue(text = "")
        cardNum = TextFieldValue(text = "")
    }

    // SAVES NEW ADDRESS TO FIRESTORE
    private fun saveAddressToFirebase(address: Address) {
        val db = FirebaseFirestore.getInstance()
        val dbCollection = db.collection(Constants.DB_Collection_Addresses)

        if (address.toString().isNotEmpty()) {
            dbCollection.add(address)
                .addOnSuccessListener { documentRef ->
                    val docId = documentRef.id
                    dbCollection.document(docId)
                        .update(hashMapOf("id" to docId) as Map<String, Any>)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.i("FB", "User data successfully created: ")
                            }
                        }.addOnFailureListener {
                            Log.w("Error", "SaveToFirebase: Error updating doc", it)
                        }
                }
        }
    }

    //UPDATES ADDRESS DETAIL(S)
    private fun updateDbWithId(
        addressId: String,
        fieldsToUpdate: Map<String, Any>,
        nextScreen: () -> Unit
    ) {
        FirebaseFirestore.getInstance()
            .collection(Constants.DB_Collection_CartItems)
            .document(addressId)
            .update(fieldsToUpdate)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    nextScreen()
                }
            }
            .addOnFailureListener {
                Log.e(TAG, "FIRESTORE UPDATE ERROR: ${it.message!!}")
                viewModelScope.launch {
                    _errorChannel.send(UiText.DynamicString(it.message.toString()))
                }
            }
    }
}