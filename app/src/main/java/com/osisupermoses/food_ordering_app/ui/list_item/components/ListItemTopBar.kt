package com.osisupermoses.food_ordering_app.ui.list_item.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.osisupermoses.food_ordering_app.R
import com.osisupermoses.food_ordering_app.ui.theme.ErrorColor

@Composable
fun ListItemTopBar(
    title: String = stringResource(id = R.string.add_product),
    navigateUp: () -> Unit
) {
    Box(
        modifier = Modifier
            .background(color = ErrorColor)
            .padding(horizontal = 10.dp)
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Icon(
            imageVector = Icons.Rounded.ArrowBack,
            contentDescription = "Arrow Back Or Close",
            tint = Color.White,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .clickable { navigateUp.invoke() }
        )
        Text(
            text = title,
            style = MaterialTheme.typography.h3.copy(
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            ),
            modifier = Modifier
                .align(Alignment.Center)
                .padding(vertical = 16.dp)
        )
    }
}