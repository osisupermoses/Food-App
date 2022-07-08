package com.osisupermoses.food_ordering_app.ui.Login.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.osisupermoses.food_ordering_app.ui.theme.black15

@Composable
fun LoginTopBar(
    text: String
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        contentAlignment = Alignment.TopStart
    ) {
        Text(
            text = text,
            style = black15.copy(
                fontSize = 50.sp,
                color = Color(0xff042E46)
            ),
            modifier = Modifier.align(Alignment.TopStart)
        )
    }
}