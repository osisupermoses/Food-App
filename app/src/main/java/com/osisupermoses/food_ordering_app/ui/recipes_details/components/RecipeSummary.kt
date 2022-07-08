package com.osisupermoses.food_ordering_app.ui.recipes_details.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.osisupermoses.food_ordering_app.R
import com.osisupermoses.food_ordering_app.domain.model.RecipesItem

@Composable
fun RecipeSummary(recipe: RecipesItem) {

    Row(
        modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(16.dp)
    ) {
        Text(
            text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = Color.White,
                        fontSize = 14.sp
                    )
                ) {
                    append("${recipe.nutrientsAmount} ")
                }
                withStyle(
                    style = SpanStyle(
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                ) {
                    append(stringResource(id = R.string.calories))
                }
            },
            modifier = Modifier
                .padding(start = 16.dp, end = 8.dp)
                .fillMaxSize()
                .weight(1f)
                .align(Alignment.CenterVertically)
        )

        Text(
            text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = Color.White,
                        fontSize = 14.sp
                    )
                ) {
                    append("${recipe.readyInMinutes} ")
                }
                withStyle(
                    style = SpanStyle(
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                ) {
                    append(stringResource(id = R.string.minutes))
                }
            },
            modifier = Modifier
                .padding(start = 16.dp, end = 8.dp)
                .fillMaxSize()
                .weight(1f)
                .align(Alignment.CenterVertically)
        )
    }
}

@Composable
@Preview
fun PreviewRecipeSummary() {
    RecipeSummary(RecipesItem())
}
