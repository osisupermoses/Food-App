package com.osisupermoses.food_ordering_app.ui.checkout.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.osisupermoses.food_ordering_app.R
import com.osisupermoses.food_ordering_app.ui.theme.ErrorColor
import com.osisupermoses.food_ordering_app.ui.theme.GoldYellow

@Composable
fun CardPlaceHolderItem(
    modifier: Modifier = Modifier,
    lastFourDigits: String,
    painter: Any?,
    selected: Boolean = false,
    borderColor: Color = GoldYellow,
    onClickCard: () -> Unit,
    onDeleteClick: () -> Unit,
    onEditCardDetails: () -> Unit
) {
    Card(
        modifier = modifier
            .size(180.dp, 120.dp)
            .clickable { onClickCard.invoke() },
        elevation = 10.dp,
        border = BorderStroke(
            width = 2.dp,
            color = if (selected) Color(0xffDD0A35) else borderColor
        ),
        shape = RoundedCornerShape(20.dp),
        backgroundColor = Color.White //GoldYellow.copy(alpha = 0.05f)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background( Color.White /*GoldYellow.copy(alpha = 0.05f)*/)
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = painter),
                contentDescription = null,
                modifier = Modifier
                    .clip(RoundedCornerShape(5.dp))
                    .padding(start = 20.dp, top = 20.dp)
                    .size(30.dp)
                    .align(Alignment.TopStart)
            )
            Box(modifier = Modifier
                .align(Alignment.TopEnd)
                .wrapContentSize()
                .background(
                    color = GoldYellow,
                    shape = RoundedCornerShape(bottomStart = 10.dp, topEnd = 20.dp)
                )
                .clickable { onEditCardDetails.invoke() }
            ) {
                Icon(
                    imageVector = Icons.Outlined.Check,
                    contentDescription = "Edit Card Icon",
                    tint = Color.Black,
                    modifier = Modifier
                        .padding(8.dp)
                        .size(20.dp)
                )
            }
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = "Edit Card Icon",
                tint = ErrorColor,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(6.dp)
                    .size(25.dp)
                    .clickable { onDeleteClick.invoke() }
            )
            Text(
                text = "**** **** **** $lastFourDigits",
                style = MaterialTheme.typography.h4.copy(Color.Black),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 10.dp)
            )
        }
    }
}

@Composable
fun AddNewCard(
    modifier: Modifier = Modifier,
    onAddNewCardClick: () -> Unit
) {
    Card(
        modifier = modifier
            .size(180.dp, 120.dp)
            .border(
                width = 2.dp,
                color = Color.Black.copy(alpha = 0.1f),
                shape = RoundedCornerShape(20.dp)
            )
            .clickable { onAddNewCardClick.invoke() },
        shape = RoundedCornerShape(20.dp),
        elevation = 10.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = stringResource(id = R.string.add_new),
                style = MaterialTheme.typography.h5.copy(color = Color.Black.copy(alpha = 0.5f))
            )
        }
    }
}

fun Modifier.dashedBorder(width: Dp, radius: Dp, color: Color) =
    drawBehind {
        drawIntoCanvas {
            val paint = Paint()
                .apply {
                    strokeWidth = width.toPx()
                    this.color = color
                    style = PaintingStyle.Stroke
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                }
            it.drawRoundRect(
                width.toPx(),
                width.toPx(),
                size.width - width.toPx(),
                size.height - width.toPx(),
                radius.toPx(),
                radius.toPx(),
                paint
            )
        }
    }

