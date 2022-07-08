package com.osisupermoses.food_ordering_app.ui.recipes_details.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.osisupermoses.food_ordering_app.R
import com.osisupermoses.food_ordering_app.domain.model.RecipesItem

@Composable
fun RecipeCaloric(recipesItem: RecipesItem) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .requiredSize(110.dp)
                    .padding(16.dp)
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawCircle(
                        color = Color.Gray,
                        style = Stroke(1.dp.toPx())
                    )
                }
                Text(
                    text = "${recipesItem.percentProtein} %",
                    color = Color.White,
                    style = MaterialTheme.typography.subtitle2,
                    modifier = Modifier.align(Alignment.Center).padding(10.dp)
                )
            }

            Text(
                text = stringResource(id = R.string.protein),
                color = Color.White,
                style = MaterialTheme.typography.subtitle2,
                fontSize = 12.sp
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .requiredSize(110.dp)
                    .padding(16.dp)
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawCircle(
                        color = Color.Gray,
                        style = Stroke(1.dp.toPx())
                    )
                }
                Text(
                    text = "${recipesItem.percentCarbs} %",
                    color = Color.White,
                    style = MaterialTheme.typography.subtitle2,
                    modifier = Modifier.align(Alignment.Center).padding(10.dp)
                )
            }
            Text(
                text = stringResource(id = R.string.carbs),
                color = Color.White,
                style = MaterialTheme.typography.subtitle2,
                fontSize = 12.sp
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .requiredSize(110.dp)
                    .padding(16.dp)
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawCircle(
                        color = Color.Gray,
                        style = Stroke(1.dp.toPx())
                    )
                }
                Text(
                    text = "${recipesItem.percentFat} %",
                    color = Color.White,
                    style = MaterialTheme.typography.subtitle2,
                    modifier = Modifier.align(Alignment.Center).padding(10.dp)
                )
            }

            Text(
                text = stringResource(id = R.string.fat),
                color = Color.White,
                style = MaterialTheme.typography.subtitle2,
                fontSize = 12.sp
            )
        }
    }
}
