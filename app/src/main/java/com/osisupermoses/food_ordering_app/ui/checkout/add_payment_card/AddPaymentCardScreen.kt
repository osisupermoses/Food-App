/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.osisupermoses.food_ordering_app.ui.checkout.add_payment_card

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.substring
import androidx.compose.ui.unit.dp
import com.osisupermoses.food_ordering_app.R
import com.osisupermoses.food_ordering_app.ui.checkout.CheckoutViewModel
import com.osisupermoses.food_ordering_app.ui.checkout.add_payment_card.components.*
import com.osisupermoses.food_ordering_app.ui.theme.GoldYellow

@ExperimentalAnimationApi
@Composable
fun AddPaymentCardScreen(
    viewModel: CheckoutViewModel,
    toCheckout: () -> Unit
) {
    val isVisaOrMaster = remember { mutableStateOf(false) }
    val textCount = remember { mutableStateOf(18) }

    Column(modifier = Modifier.fillMaxSize()) {
        PaymentCard(
            viewModel.cardHolderName,
            viewModel.cardNum,
            viewModel.expiryNumber,
            viewModel.cvcNumber
        )
        LazyColumn(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            item {
                InputItem(
                    textFieldValue = viewModel.cardHolderName,
                    label = stringResource(id = R.string.card_holder_name),
                    onTextChanged = { viewModel.cardHolderName = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                )
            }

            item {
                InputItem(
                    textFieldValue = viewModel.cardNum,
                    label = stringResource(id = R.string.card_holder_number),
                    keyboardType = KeyboardType.Number,
                    onTextChanged = {
                        isVisaOrMaster.value = it.text.startsWith("4") || it.text.startsWith("5")
                        if (isVisaOrMaster.value) {
                            textCount.value = 16
                        } else textCount.value = 18
                        if (it.text.count() <= textCount.value) {
                            viewModel.cardNum = it
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    visualTransformation = {
                        if (isVisaOrMaster.value) creditCardFilter(it)
                        else creditCardFilterForVerve(it)
                    }
                )
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    InputItem(
                        textFieldValue = viewModel.expiryNumber,
                        label = stringResource(id = R.string.expiry_date),
                        keyboardType = KeyboardType.Number,
                        onTextChanged = {
                            if (it.text.count() <= 4)
                            viewModel.expiryNumber = it
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp),
                        visualTransformation = ExpiryCardFilter
                    )
                    InputItem(
                        textFieldValue = viewModel.cvcNumber,
                        label = stringResource(id = R.string.cvc),
                        keyboardType = KeyboardType.Number,
                        onTextChanged = {
                            if (it.text.count() <= 3)
                            viewModel.cvcNumber = it
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp)
                    )
                }
            }
            item {
                Button(
                    onClick = { toCheckout.invoke() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = ButtonDefaults.buttonColors(GoldYellow)
                ) {
                    Text(
                        text = stringResource(id = R.string.save),
                        color = MaterialTheme.colors.onPrimary,
                        modifier = Modifier.padding(horizontal = 30.dp, vertical = 8.dp)
                    )
                }
            }
        }
    }
}
