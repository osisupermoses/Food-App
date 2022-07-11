package com.osisupermoses.food_ordering_app.ui.menu.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import com.osisupermoses.food_ordering_app.ui.theme.spacing

@Composable
fun MenuTopBar(
    modifier: Modifier = Modifier,
    topIcon: Painter?,
    storeAddress: String,
    onSearchClick: () -> Unit,
    onDrawerClick: () -> Unit
) {

    Box(
        modifier = Modifier
            .padding(MaterialTheme.spacing.medium)
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        if (topIcon != null) {
            Image(
                painter = topIcon,
                contentDescription = "DrawerIcon",
                modifier = modifier
                    .size(30.dp)
                    .align(Alignment.CenterStart)
                    .clickable { onDrawerClick.invoke() }
            )
        }
        Text(
            text = storeAddress,
            style = MaterialTheme.typography.h6.copy(
                color = Color.LightGray
            ),
            modifier = Modifier.align(Alignment.Center)
        )
        Icon(
            imageVector = Icons.Rounded.Search,
            tint = Color.White,
            contentDescription = "Search Icon",
            modifier = Modifier
                .size(25.dp)
                .align(Alignment.CenterEnd)
                .clickable { onSearchClick.invoke() }
        )
    }
}