package com.osisupermoses.food_ordering_app.ui.cart.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.osisupermoses.food_ordering_app.R
import com.osisupermoses.food_ordering_app.ui.checkout.components.CheckoutButton
import com.osisupermoses.food_ordering_app.ui.theme.ErrorColor

@Composable
fun CheckoutComposable(
    modifier: Modifier = Modifier,
    subTotal: String,
    shippingCharges: String,
    totalAmount: String,
    btnEnabled: Boolean,
    currencySymbol: String = "₦",
    onCheckoutClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .padding(20.dp)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .wrapContentSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(id = R.string.subtotal),
                    style = MaterialTheme.typography.h4.copy(
                        color = Color.DarkGray,
                    )
                )
                Text(
                    text = currencySymbol + subTotal,
                    style = MaterialTheme.typography.h4.copy(
                        color = Color.DarkGray,
                    )
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(id = R.string.delivery_charge),
                    style = MaterialTheme.typography.h4.copy(
                        color = Color.DarkGray,
                    )
                )
                Text(
                    text = currencySymbol + shippingCharges,
                    style = MaterialTheme.typography.h4.copy(
                        color = Color.DarkGray,
                    )
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(id = R.string.total),
                    style = MaterialTheme.typography.h4.copy(
                        color = Color.DarkGray,
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = currencySymbol + totalAmount,
                    style = MaterialTheme.typography.h4.copy(
                        color = Color.DarkGray,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
            Spacer(modifier = Modifier.height(40.dp))
            Button(
                onClick = onCheckoutClick,
                colors = ButtonDefaults.buttonColors(ErrorColor),
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .height(48.dp),
                shape = RoundedCornerShape(10.dp),
                enabled = btnEnabled,
            ) {
                Text(
                    text = stringResource(id = R.string.checkout).uppercase(),
                    color = Color.White,
                    style = MaterialTheme.typography.button.copy(
                        fontSize = 18.sp
                    ),
                    letterSpacing = 0.00.em
                )
            }
        }
    }
}


@Composable
@Preview
fun CartCheckoutPreview() {
    CheckoutComposable(
        subTotal = "$200",
        shippingCharges = "$20",
        totalAmount = "$220",
        btnEnabled = true
    )
}