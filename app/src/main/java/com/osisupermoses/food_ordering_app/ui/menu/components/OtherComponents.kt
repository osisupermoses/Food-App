package com.osisupermoses.food_ordering_app.ui.menu.components

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material.icons.rounded.AccessTime
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.request.ImageResult
import com.osisupermoses.food_ordering_app.R
import com.osisupermoses.food_ordering_app.ui.theme.spacing

@Composable
fun Header(
    menuTitle: String
) {
    Text(
        text = menuTitle,
        style = MaterialTheme.typography.h2.copy(
            color = Color.Black,
            fontSize = 30.sp
        ),
        modifier = Modifier
            .padding(horizontal = MaterialTheme.spacing.medium)
            .padding(
                top = MaterialTheme.spacing.medium,
                bottom = MaterialTheme.spacing.large
            )
    )
}

@Composable
fun MenuBoxItem(
    modifier: Modifier = Modifier,
    iconModifier: Modifier = Modifier,
    icon: ImageVector? = null,
    text: String,
    textStyle: TextStyle,
    rowPadding: Dp = 12.dp,
    hasGoldBackground: Boolean = false,
    backgroundColor: Color,
    onItemClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .background(
                color = if (hasGoldBackground) backgroundColor else Color.White,
                shape = RoundedCornerShape(MaterialTheme.spacing.small)
            )
            .border(
                width = 1.dp,
                color = Color.LightGray,
                shape = RoundedCornerShape(MaterialTheme.spacing.small)
            )
            .clip(RoundedCornerShape(MaterialTheme.spacing.small))
            .clickable {
                onItemClick.invoke()
            },
        contentAlignment = Center
    ) {
        Row(
            modifier = Modifier
                .padding(rowPadding)
                .fillMaxWidth(),
            verticalAlignment = CenterVertically
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = "MenuTop Icon",
                    modifier = iconModifier
                )
            }
            Spacer(modifier = Modifier.width(MaterialTheme.spacing.extraSmall))
            Text(
                text = text,
                style = textStyle,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun PopularItem(
    modifier: Modifier,
    image: Any?,
    currencySymbol: String = "₦",
    foodName: String,
    price: Double,
    estDeliveryTime: String,
    orderRating: Double,
    context: Context,
    onRatingClick: () -> Unit,
    onPopularItemClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onPopularItemClick.invoke() }
    ) {
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
                .clip(RoundedCornerShape(MaterialTheme.spacing.semiLarge))
                .size(
                    height = 170.dp,
                    width = 250.dp
                ),
            contentScale = ContentScale.FillBounds
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraSmall))
        Text(
            text = foodName,
            style = MaterialTheme.typography.h3.copy(
                fontWeight = FontWeight.Medium
            ),
            modifier = Modifier.align(Alignment.Start)
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = CenterVertically
        ) {
            MenuBoxItem(
                icon = null,
                text = "$currencySymbol$price",
                textStyle = MaterialTheme.typography.subtitle2.copy(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                ),
                backgroundColor = Color.White,
                rowPadding = MaterialTheme.spacing.small
            )
            Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
            MenuBoxItem(
                icon = Icons.Rounded.AccessTime,
                iconModifier = Modifier.size(MaterialTheme.spacing.semiLarge),
                text = "$estDeliveryTime Min",
                textStyle = MaterialTheme.typography.subtitle2.copy(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                ),
                backgroundColor = Color.White,
                rowPadding = MaterialTheme.spacing.small
            )
            Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
            MenuBoxItem(
                icon = Icons.Outlined.StarOutline,
                iconModifier = Modifier.size(MaterialTheme.spacing.semiLarge),
                text = "$orderRating",
                textStyle = MaterialTheme.typography.subtitle2.copy(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                ),
                onItemClick = { onRatingClick() },
                backgroundColor = Color.White,
                rowPadding = MaterialTheme.spacing.small
            )
        }
    }
}

@Composable
fun RestaurantItem(
    modifier: Modifier,
    frontalImage: Any?,
    restaurantName: String,
    restaurantReviewScore: Double,
    context: Context,
    onReviewScoreClick: () -> Unit,
    onRestaurantItemClick: () -> Unit
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onRestaurantItemClick.invoke() }
    ) {
        AsyncImage(
           model = ImageRequest
               .Builder(context = context)
               .crossfade(true)
               .data(frontalImage)
               .build(),
            filterQuality = FilterQuality.High,
            contentDescription = "Item Image",
            modifier = Modifier
                .clip(RoundedCornerShape(MaterialTheme.spacing.medium))
                .size(
                    height = 170.dp,
                    width = 250.dp
                ),
            contentScale = ContentScale.FillBounds
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraSmall))
        Text(
            text = restaurantName,
            style = MaterialTheme.typography.h3.copy(
                fontWeight = FontWeight.Medium
            )
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraSmall))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = CenterVertically
        ) {
            Text(
                text = stringResource(R.string.seller_score),
                style = MaterialTheme.typography.subtitle2.copy(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                ),
                modifier = Modifier.padding(end = MaterialTheme.spacing.small)
            )
            MenuBoxItem(
                icon = Icons.Outlined.StarOutline,
                iconModifier = Modifier.size(MaterialTheme.spacing.semiLarge),
                text = "$restaurantReviewScore",
                textStyle = MaterialTheme.typography.subtitle2.copy(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                ),
                onItemClick = { onReviewScoreClick.invoke() },
                backgroundColor = Color.White,
                rowPadding = MaterialTheme.spacing.small
            )
        }
    }
}