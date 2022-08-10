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
        cardNumber: String,
        cardExpiry: String,
        cvv: String,
        amount: String,
        customerEmail: String,
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

//        val cardExpiryArray = cardExpiry.split("/").toTypedArray()
        val expiryMonth = cardExpiry.substring(0,2).toInt()
        val expiryYear = cardExpiry.substring(2, 4).toInt()
        val amountInKobo = amount.toDouble() * 100
        val card = Card(cardNumber, expiryMonth, expiryYear, cvv)

        val charge = Charge()
        charge.amount = amountInKobo.toInt()
        charge.email = customerEmail
        charge.card = card

        PaystackSdk.chargeCard(activityContext as Activity, charge, object : Paystack.TransactionCallback {
            override fun onSuccess(transaction: Transaction) {
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