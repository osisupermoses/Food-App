package com.osisupermoses.food_ordering_app.ui.recipes_details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.osisupermoses.food_ordering_app.R
import com.osisupermoses.food_ordering_app.ui.recipes_details.components.*
import com.osisupermoses.food_ordering_app.ui.ui_common.Button
import com.osisupermoses.food_ordering_app.util.loading.LoadingContent
import com.osisupermoses.food_ordering_app.util.toasty

@Composable
fun RecipesDetailsScreen(
    viewModel: RecipeDetailsViewModel = hiltViewModel(),
    navigateUp: () -> Unit,
    toCheckoutScreen: (String, String) -> Unit,
) {
    val recipeDetails by viewModel.viewState.collectAsState()
    val scaffoldState = rememberScaffoldState()

    var addToCart by remember { mutableStateOf(R.string.add_to_cart) }
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.errorChannel.collect { error ->
            scaffoldState.snackbarHostState.showSnackbar(
                error.asString(context)
            )
        }
    }

    Scaffold(
        scaffoldState = scaffoldState
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            LoadingContent(recipeDetails.isLoading) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = MaterialTheme.colors.background)
                ) {
                    item { recipeDetails.recipe?.let { RecipesHeader(it, navigateUp) } }
                    item {
                        recipeDetails.recipe?.let {
                            RecipeOptions(it) { recipe ->
                                viewModel.saveRecipe(recipe)
                            }
                        }
                    }
                    item { RecipeDivider() }
                    item { recipeDetails.recipe?.let { RecipeSummary(it) } }
                    item { RecipeDivider() }
                    item { recipeDetails.recipe?.let { RecipeTags(it) } }
                    item { recipeDetails.recipe?.let { RecipeCaloric(it) } }
                    item { RecipeDivider() }
                    item { RecipeIngredientTitle() }
                    items(recipeDetails.recipe?.ingredientOriginalString ?: listOf()) { recipe ->
                        RecipeIngredientItem(recipe)
                    }
                    item { RecipeDivider() }
                    item { RecipeSteps(recipeDetails.recipe?.step) }
                    item { Spacer(modifier = Modifier.height(30.dp)) }
                    item {
                        Button(
                            modifier = Modifier.padding(horizontal = 30.dp),
                            text = stringResource(addToCart)
                        ) {
                            toasty(context, context.getString(R.string.item_added))
                            addToCart = R.string.view_in_cart
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        Button(
                            modifier = Modifier.padding(horizontal = 30.dp),
                            text = stringResource(R.string.order_now)
                        ) {
                            recipeDetails.recipe?.let {
                                toCheckoutScreen.invoke(
                                    it.price.toString(),
                                    recipeDetails.recipe!!.deliveryFee.toString()
                                )
                            }
                        }
                    }
                    item { Spacer(modifier = Modifier.height(30.dp)) }
                }
            }
        }
    }
}

@Composable
private fun RecipeDivider() {
    Divider(
        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
        color = MaterialTheme.colors.onSurface.copy(alpha = 0.08f)
    )
}
