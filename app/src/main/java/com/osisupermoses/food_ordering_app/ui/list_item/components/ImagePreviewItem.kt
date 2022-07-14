package com.osisupermoses.food_ordering_app.ui.list_item.components

import android.net.Uri
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.osisupermoses.food_ordering_app.R
import com.osisupermoses.food_ordering_app.ui.theme.ErrorColor

@Composable
fun ImagePreviewItem(
    uri: Uri,
    height: Dp,
    width: Dp,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        AsyncImage(
            model = uri,
            placeholder = rememberAsyncImagePainter(model = R.drawable.imageplaceholder),
            contentDescription = "Image",
            modifier = Modifier
                .width(width)
                .height(height),
            contentScale = ContentScale.Crop
        )
        IconButton(
            onClick = { onClick() },
            modifier = Modifier.align(Alignment.BottomStart)
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "",
                modifier = Modifier
                    .padding(4.dp)
                    .size(20.dp),
                tint = Color.White
            )
        }
    }
}