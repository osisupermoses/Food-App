package com.osisupermoses.food_ordering_app.util.paystack

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import co.paystack.android.Paystack
import co.paystack.android.PaystackSdk
import co.paystack.android.Transaction
import co.paystack.android.model.Card
import co.paystack.android.model.Charge
import com.osisupermoses.food_ordering_app.BuildConfig

class CheckoutActivity(private val context: Context) {

    fun initializePaystack() {
        PaystackSdk.initialize(context.applicationContext)
        PaystackSdk.setPublicKey(BuildConfig.PSTK_PUBLIC_KEY)
    }

    fun paystackGateway(
        cardNumber: String = "4084084084084081",
        cardExpiry: String = "07/23",
        cvv: String = "408",
        amount: String,
        customerEmail: String = "osisuper_moses@yahoo.com",
        activityContext: Context,
        onSuccess: (String) -> Unit,
        onFailed: (Throwable) -> Unit
    ) {
        performCharge(
            cardNumber,
            cardExpiry,
            cvv,
            amount,
            customerEmail,
            activityContext,
            onSuccess,
            onFailed
        )
    }

//    private fun initializeFormVariables(
//        mCardNumber: String,
//        mCardExpiry: String,
//        mCardCVV: String
//    ) {
////        // this is used to add a forward slash (/) between the cards expiry month
////        // and year (11/21). After the month is entered, a forward slash is added
////        // before the year
////        mCardExpiry.editText!!.addTextChangedListener(object : TextWatcher {
////            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
////
////            }
////
////            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
////
////            }
////
////            override fun afterTextChanged(s: Editable?) {
////                if (s.toString().length == 2 && !s.toString().contains("/")) {
////                    s!!.append("/")
////                }
////            }
////
////        })
//
////        val button = findViewById<Button>(R.id.btn_make_payment)
//        button.setOnClickListener { performCharge() }
//    }

    private fun performCharge(
        cardNumber: String,
        cardExpiry: String,
        cvv: String,
        amount: String,
        customerEmail: String,
        activityContext: Context,
        onSuccess: (String) -> Unit,
        onFailed: (Throwable) -> Unit
    ) {

        val cardExpiryArray = cardExpiry.split("/").toTypedArray()
        val expiryMonth = cardExpiryArray[0].toInt()
        val expiryYear = cardExpiryArray[1].toInt()
//        var amount = intent.getIntExtra(getString(R.string.meal_name), 0)
//        amount *= 100
        val amountInKobo = amount.toDouble() * 100

        val card = Card(cardNumber, expiryMonth, expiryYear, cvv)

        val charge = Charge()
        charge.amount = amountInKobo.toInt()
        charge.email = customerEmail
        charge.card = card

        PaystackSdk.chargeCard(activityContext as Activity, charge, object : Paystack.TransactionCallback {
            override fun onSuccess(transaction: Transaction) {
//                parseResponse(transaction.reference)
                onSuccess(transaction.reference)
            }

            override fun beforeValidate(transaction: Transaction) {
                Log.d("Checkout Activity", "beforeValidate: " + transaction.reference)
            }

            override fun onError(error: Throwable, transaction: Transaction) {
                Log.d("Checkout Activity", "onError: " + error.localizedMessage)
                Log.d("Checkout Activity", "onError: $error")
                onFailed.invoke(error)
            }
        })
    }

    private fun parseResponse(transactionReference: String) {
        val message = "Payment Successful - $transactionReference"
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        Log.d("Payment success: ", transactionReference)
    }
}