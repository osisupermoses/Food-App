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
    toCartScreen: () -> Unit,
    toCheckoutScreen: (String, String) -> Unit,
) {
    val recipeDetails by viewModel.viewState.collectAsState()
    val scaffoldState = rememberScaffoldState()

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
                    item { viewModel.recipeItem?.let {
                            RecipesHeader(
                                recipesItem = it,
                                images = viewModel.getRecipeDetailsImages(it), //to be done for paging images
                                navigateUp = navigateUp
                            )
                        }
                    }
                    item {
                        viewModel.recipeItem?.let {
                            RecipeOptions(it) { recipe ->
                                viewModel.saveRecipe(recipe)
                            }
                        }
                    }
                    item { RecipeDivider() }
                    item { viewModel.recipeItem?.let { RecipeSummary(it) } }
                    item { RecipeDivider() }
                    item { viewModel.recipeItem?.let { RecipeTags(it) } }
                    item { viewModel.recipeItem?.let { RecipeCaloric(it) } }
                    item { RecipeDivider() }
                    item { RecipeIngredientTitle() }
                    items(viewModel.recipeItem?.ingredientOriginalString ?: listOf()) { recipe ->
                        RecipeIngredientItem(recipe)
                    }
                    item { RecipeDivider() }
                    item { RecipeSteps(viewModel.recipeItem?.step) }
                    item { Spacer(modifier = Modifier.height(30.dp)) }
                    item {
                        Button(
                            modifier = Modifier.padding(horizontal = 30.dp),
                            text = stringResource(viewModel.addToCart.value)
                        ) {
                            viewModel.onAddToCart(toCartScreen)
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        Button(
                            modifier = Modifier.padding(horizontal = 30.dp),
                            text = stringResource(R.string.order_now)
                        ) {
                            viewModel.recipeItem?.let {
                                toCheckoutScreen.invoke(
                                    it.price.toString(),
                                    viewModel.recipeItem?.deliveryFee.toString()
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
