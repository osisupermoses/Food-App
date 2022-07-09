package com.osisupermoses.food_ordering_app.ui.recipes_details.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.IosShare
import androidx.compose.material.icons.filled.Stars
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.osisupermoses.food_ordering_app.R
import com.osisupermoses.food_ordering_app.domain.model.RecipesItem
import com.osisupermoses.food_ordering_app.ui.theme.ErrorColor

@Composable
fun RecipeOptions(
    recipe: RecipesItem,
    onSave: (RecipesItem) -> Unit
) {
    Row(
        modifier = Modifier.padding(start = 8.dp, end = 8.dp)
    ) {
        TextButton(
            modifier = Modifier.weight(1f)
                .align(Alignment.CenterVertically),
            onClick = {}
        ) {
            Icon(
                modifier = Modifier.padding(end = 4.dp),
                imageVector = Icons.Filled.Favorite,
                contentDescription = null,
                tint = ErrorColor
            )
            Text(
                text = "${recipe.aggregateLikes}",
                color = Color.White,
                style = MaterialTheme.typography.subtitle2,
                fontSize = 12.sp
            )
        }
        TextButton(
            modifier = Modifier.weight(1f)
                .align(Alignment.CenterVertically),
            onClick = { }
        ) {
            Icon(
                modifier = Modifier.padding(end = 4.dp),
                imageVector = Icons.Filled.Stars,
                contentDescription = null,
                tint = Color.White
            )
            Text(
                text = "${recipe.spoonacularScore?.toInt()}",
                color = Color.White,
                style = MaterialTheme.typography.subtitle2,
                fontSize = 12.sp
            )
        }

        TextButton(
            modifier = Modifier.weight(1f)
                .align(Alignment.CenterVertically),
            onClick = {
                onSave(recipe)
            }
        ) {
            Icon(
                modifier = Modifier.padding(end = 4.dp),
                imageVector = Icons.Filled.BookmarkBorder,
                contentDescription = null,
                tint = Color.White
            )
            Text(
                text = stringResource(id = R.string.save),
                color = Color.White,
                style = MaterialTheme.typography.subtitle2,
                fontSize = 12.sp
            )
        }

        TextButton(
            modifier = Modifier.weight(1f)
                .align(Alignment.CenterVertically),
            onClick = { }
        ) {
            Icon(
                modifier = Modifier.padding(end = 4.dp),
                imageVector = Icons.Filled.IosShare,
                contentDescription = null,
                tint = Color.White
            )
            Text(
                text = stringResource(id = R.string.share),
                color = Color.White,
                style = MaterialTheme.typography.subtitle2,
                fontSize = 12.sp
            )
        }
    }
    Spacer(modifier = Modifier.height(10.dp))
}
