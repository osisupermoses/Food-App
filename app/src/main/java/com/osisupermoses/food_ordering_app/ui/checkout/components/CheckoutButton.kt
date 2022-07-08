package com.osisupermoses.food_ordering_app.ui.checkout.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.SpaceBetween
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.osisupermoses.food_ordering_app.R
import com.osisupermoses.food_ordering_app.ui.theme.GoldYellow

@Composable
fun CheckoutButton(
    modifier: Modifier = Modifier,
    leftText: String = stringResource(id = R.string.checkout),
    totalAmt: String,
    color: Color = GoldYellow,
    shape: Shape = CircleShape,
    enabled: Boolean = true,
    onButtonClick: () -> Unit,
) {
    Button(
        onClick = onButtonClick,
        colors = ButtonDefaults.buttonColors(color),
        modifier = modifier.height(48.dp),
        shape = shape,
        enabled = enabled,
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .align(CenterVertically)
                .padding(
                    horizontal = 5.dp,
                    vertical = 5.dp
                ),
            horizontalArrangement = SpaceBetween
        ) {
            Text(
                text = leftText,
                color = Color.Black,
                style = MaterialTheme.typography.button,
                letterSpacing = 0.00.em
            )
            Text(
                text = totalAmt,
                color = Color.Black,
                style = MaterialTheme.typography.button,
                letterSpacing = 0.00.em
            )
        }
    }
}