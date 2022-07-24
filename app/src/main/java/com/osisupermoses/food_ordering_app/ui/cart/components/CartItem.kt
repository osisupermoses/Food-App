package com.osisupermoses.food_ordering_app.ui.cart.components

import android.content.Context
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.osisupermoses.food_ordering_app.R
import com.osisupermoses.food_ordering_app.ui.theme.ErrorColor
import com.osisupermoses.food_ordering_app.ui.theme.spacing

@Composable
fun CartHolderItem(
    modifier: Modifier = Modifier,
    context: Context,
    image: Any?,
    currencySymbol: String = "₦",
    cartItemTitle: String,
    cartItemAmount: String,
    cartItemQuantity: String,
    onRemoveQuantityClick: () -> Unit = {},
    onAddQuantityClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {},
    onItemClick: () -> Unit = {},
) {

    Box(
        modifier = modifier
            .padding(20.dp)
            .fillMaxWidth()
            .clickable { onItemClick.invoke() }
    ) {
        Row {
            AsyncImage(
                model = ImageRequest
                    .Builder(context = context)
                    .crossfade(true)
                    .data(image)
                    .build(),
                filterQuality = FilterQuality.High,
                placeholder = rememberAsyncImagePainter(model = R.drawable.imageplaceholder),
                contentDescription = "Item Image",
                modifier = Modifier
                    .clip(RoundedCornerShape(MaterialTheme.spacing.small))
                    .size(
                        height = 70.dp,
                        width = 80.dp
                    ),
                contentScale = ContentScale.FillBounds
            )
            Spacer(modifier = Modifier.width(10.dp))
            CartTitleItem(
                cartItemTitle = cartItemTitle,
                cartItemAmount = currencySymbol + cartItemAmount,
                cartItemQuantity = cartItemQuantity,
                onRemoveQuantityClick = onRemoveQuantityClick,
                onAddQuantityClick = onAddQuantityClick
            )
        }
        Icon(
            imageVector = Icons.Filled.Delete,
            contentDescription = "Edit Card Icon",
            tint = ErrorColor,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(20.dp)
                .clickable { onDeleteClick.invoke() }
        )
    }
}

@Composable
fun CartTitleItem(
    modifier: Modifier = Modifier,
    cartItemTitle: String,
    cartItemAmount: String,
    cartItemQuantity: String,
    onRemoveQuantityClick: () -> Unit,
    onAddQuantityClick: () -> Unit,
) {

    Column(
        modifier = modifier
            .padding(vertical = 10.dp)
            .wrapContentSize(),
        horizontalAlignment = Alignment.Start,
    ) {
        Text(
            text = cartItemTitle,
            style = MaterialTheme.typography.h6,
            color = Color(0xffAAAAAA)
        )
        Row(
            modifier = Modifier
                .padding(end = 10.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = cartItemAmount,
                style = MaterialTheme.typography.h6.copy(
                    color = Color.Black,
                    fontWeight = FontWeight.SemiBold
                )
            )
            Row(
                modifier = Modifier
                    .wrapContentSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Icon(
                    imageVector = Icons.Rounded.Remove,
                    contentDescription = "Remove Quantity Icon",
                    modifier = Modifier
                        .align(CenterVertically)
                        .padding(
                            vertical = 5.dp,
                            horizontal = 8.dp
                        )
                        .size(20.dp)
                        .clickable { onRemoveQuantityClick.invoke() },

                )
                Text(
                    text = cartItemQuantity,
                    style = MaterialTheme.typography.h5.copy(
                        color = Color(0xffAAAAAA)
                    ),
                    modifier = Modifier
                        .align(CenterVertically)
                        .border(
                            width = 0.5.dp,
                            color = Color(0xff080808)
                        )
                        .padding(
                            vertical = 5.dp,
                            horizontal = 16.dp
                        )
                )
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = "Add Quantity Icon",
                    modifier = Modifier
                        .align(CenterVertically)
                        .padding(
                            vertical = 5.dp,
                            horizontal = 8.dp
                        )
                        .size(20.dp)
                        .clickable { onAddQuantityClick.invoke() }
                    )
            }
        }
    }
}

@Composable
@Preview
fun Preview() {

    CartHolderItem(
        context = LocalContext.current,
        image = R.drawable.imageplaceholder,
        cartItemTitle = "Yam",
        cartItemAmount = "$100",
        cartItemQuantity = "1"
    )
}