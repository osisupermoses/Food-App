package com.osisupermoses.food_ordering_app.ui.menu.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.osisupermoses.food_ordering_app.R
import com.osisupermoses.food_ordering_app.ui.theme.ErrorColor
import com.osisupermoses.food_ordering_app.ui.theme.GoldYellow
import com.osisupermoses.food_ordering_app.ui.theme.background

@Composable
fun DrawerHeader(
    isAdmin: Boolean,
    accountName: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                vertical = 64.dp,
                horizontal = 20.dp
            )
            .background(color = background.copy(0.8f)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .border(
                    width = 2.dp,
                    color = GoldYellow,
                    shape = CircleShape
                )
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = accountName,
                fontSize = 20.sp,
                color = Color.White,
            )
            Text(
                text =
                    if (isAdmin)stringResource(R.string.admin_account)
                    else stringResource(R.string.user_account),
                fontSize = 10.sp,
                fontStyle = FontStyle.Italic,
                color = Color.White,
            )
        }
    }
}

@Composable
fun DrawerBody(
    modifier: Modifier = Modifier,
    items: List<MenuItem>,
    itemTextStyle: TextStyle = TextStyle(fontSize = 18.sp, color = GoldYellow),
    onItemClick: (MenuItem) -> Unit
) {
    LazyColumn(modifier) {
        items(items) { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onItemClick(item)
                    }
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = item.icon,
                    tint = GoldYellow,
                    contentDescription = item.contentDescription
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = item.title,
                    style = itemTextStyle,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun DrawerBottom(
    modifier: Modifier = Modifier,
    onLogOUtClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(90.dp)
            .background(color = background.copy(0.8f))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(
                    horizontal = 20.dp,
                    vertical = 20.dp
                )
                .border(
                    width = 1.dp,
                    color = GoldYellow,
                    shape = RoundedCornerShape(8.dp)
                )
                .clickable { onLogOUtClick.invoke() },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.log_out),
                style = MaterialTheme.typography.h3.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )
        }
    }
}