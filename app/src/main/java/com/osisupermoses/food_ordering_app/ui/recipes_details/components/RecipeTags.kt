package com.osisupermoses.food_ordering_app.ui.recipes_details.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.osisupermoses.food_ordering_app.R
import com.osisupermoses.food_ordering_app.domain.model.RecipesItem
import com.osisupermoses.food_ordering_app.util.widget.ChipView

@Composable
fun RecipeTags(recipe: RecipesItem) {
    val recipeTags = createTagsList(recipe)

    LazyRow(
        contentPadding = PaddingValues(vertical = 8.dp, horizontal = 16.dp)
    ) {
        items(
            recipeTags.filter {
                it.first == true
            }
        ) { tag ->
            ChipView(
                chipMessage = tag.second,
                color = Color.White,
            )
        }
    }
}

@Composable
private fun createTagsList(recipe: RecipesItem) = listOf(
    Pair(
        recipe.cheap,
        stringResource(id = R.string.cheap)
    ),
    Pair(
        recipe.glutenFree,
        stringResource(id = R.string.glutenFree)
    ),
    Pair(
        recipe.vegetarian,
        stringResource(id = R.string.vegetarian)
    ),
    Pair(
        recipe.vegan,
        stringResource(id = R.string.vegan)
    ),
    Pair(
        recipe.dairyFree,
        stringResource(id = R.string.dairyFree)
    ),
    Pair(
        recipe.veryHealthy,
        stringResource(id = R.string.veryHealthy)
    ),
    Pair(
        recipe.veryPopular,
        stringResource(id = R.string.veryPopular)
    ),
    Pair(
        recipe.sustainable,
        stringResource(id = R.string.sustainable)
    )
)
