
package com.osisupermoses.food_ordering_app.ui.recipes_details.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.osisupermoses.food_ordering_app.R

@Composable
fun RecipeIngredientTitle() {
    Text(
        text = stringResource(id = R.string.ingredients),
        style = MaterialTheme.typography.h6,
        modifier = Modifier.padding(16.dp).fillMaxWidth(),
        color = Color.White
    )
}

@Composable
fun RecipeIngredientItem(ingredientTitle: String) {
    Text(
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 16.dp),
        text = "✓  $ingredientTitle",
        style = MaterialTheme.typography.subtitle2,
        color = Color.White
    )
}
