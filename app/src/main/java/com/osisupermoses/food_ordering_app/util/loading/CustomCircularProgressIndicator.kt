package com.osisupermoses.food_ordering_app.util.loading

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.osisupermoses.food_ordering_app.ui.theme.GoldYellow
import com.osisupermoses.food_ordering_app.ui.theme.black15

/**
 * A loading progress indicator.
 *
 * This will also take a [message] to show it with the progress indicator.
 */

@Composable
fun CustomCircularProgressIndicator(message: String = "") {
    if(message.isBlank()) {
        Dialog(onDismissRequest = { /*TODO*/ }) {
            Card(
                backgroundColor = Color.White,
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .fillMaxWidth(0.5f)
                    .border(
                        width = 1.dp,
                        color = Color(0xffDD0A35)
                    )
                    .background(color = GoldYellow),
                elevation = 10.dp
            ) {
                Box(
                    modifier = Modifier.padding(15.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(color = GoldYellow)
                }
            }
        }
    } else {
        Card(
            backgroundColor = Color.White,
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .fillMaxWidth(0.5f)
                .background(color = GoldYellow),
            elevation = 10.dp,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(15.dp),
            ) {
                CircularProgressIndicator(color = GoldYellow)
                SizedBox(width = 15)
                Text(
                    message,
                    style = black15
                )
            }
        }
    }
}

@Composable
fun LoadingContent(
    isLoading: Boolean = false,
    message: String = "",
    content: @Composable () -> Unit = {},
) {
    if (isLoading) {
    CustomCircularProgressIndicator(message)
    } else {
        content()
    }
}