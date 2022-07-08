package com.osisupermoses.food_ordering_app.ui.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.osisupermoses.food_ordering_app.R
import com.osisupermoses.food_ordering_app.ui.theme.GoldYellow
import com.osisupermoses.food_ordering_app.ui.theme.spacing
import com.osisupermoses.food_ordering_app.ui.menu.components.*
import com.osisupermoses.food_ordering_app.ui.ui_common.SectionHeader
import com.osisupermoses.food_ordering_app.util.loading.CustomCircularProgressIndicator

@Composable
fun MenuScreen(
    viewModel: MenuScreenViewModel = hiltViewModel(),
    toFoodDetailsScreen: (Int) -> Unit,
    toRestaurantDetailScreen: () -> Unit
) {

    val scaffoldState = rememberScaffoldState()
    val state = viewModel.state.value
    val context = LocalContext.current
    
    LaunchedEffect(key1 = true) {
        viewModel.errorChannel.collect { error ->
            scaffoldState.snackbarHostState.showSnackbar(
                message = error.asString(context)
            )
        }
    }

    Scaffold(
        topBar = {
            MenuTopBar(
                topIcon = painterResource(R.drawable.hamburger_icon),
                storeAddress = "University of Ibadan, NG",
                onSearchClick = {  }
            ) {

            }
        },
        scaffoldState = scaffoldState
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .background(color = Color.White)
                .fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                item { Spacer(modifier = Modifier.height(MaterialTheme.spacing.small)) }
                item {
                    Header(menuTitle = stringResource(R.string.fast_and_delicious))
                    LazyRow {
                        item {
                            Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))
                        }
                        state.headerList?.let { listOfMenuItem ->
                            itemsIndexed(listOfMenuItem.toList()) { index, menuItem ->
                                MenuBoxItem(
                                    modifier = Modifier.wrapContentSize(),
                                    icon = menuItem.icon,
                                    text = menuItem.header,
                                    textStyle = MaterialTheme.typography.h6.copy(
                                        color = Color.Black,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Normal
                                    ),
                                    hasGoldBackground = index == viewModel.topClickIndex.value,
                                    backgroundColor = GoldYellow
                                ) {
                                    viewModel.topClickIndex.value = index
                                }
                                Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
                            }
                        }
                        item {
                            Spacer(modifier = Modifier.width(12.dp))
                        }
                    }
                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))
                    SectionHeader(sectionTitle = stringResource(id = R.string.popular))
                    LazyRow {
                        item {
                            Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))
                        }
                        state.foodList?.let { foodList ->
                            itemsIndexed(foodList) { index, food ->
                                PopularItem(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(MaterialTheme.spacing.small))
                                        .wrapContentHeight(),
                                    image = food.image,
                                    foodName = food.name,
                                    price = food.price,
                                    estDeliveryTime = food.estDeliveryTime,
                                    orderRating = food.orderRating,
                                    onRatingClick = {}
                                ) {
                                    toFoodDetailsScreen.invoke(food.id!!)
                                }
                                Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
                            }
                        }
                        item {
                            Spacer(modifier = Modifier.width(12.dp))
                        }
                    }
                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))
                    SectionHeader(sectionTitle = stringResource(id = R.string.all_restaurants))
                    LazyRow {
                        item {
                            Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))
                        }
                        state.restaurantList?.let { restaurants ->
                            itemsIndexed(restaurants) { index, restaurant ->
                                RestaurantItem(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(MaterialTheme.spacing.small))
                                        .wrapContentHeight(),
                                    frontalImage = restaurant.frontalImage,
                                    restaurantName = restaurant.restaurantName,
                                    restaurantReviewScore = restaurant.restaurantReviews,
                                    onReviewScoreClick = { }
                                ) {
                                    toRestaurantDetailScreen.invoke()
                                }
                                Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
                            }
                        }
                        item {
                            Spacer(modifier = Modifier.width(12.dp))
                        }
                    }
                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))
                }
            }
            if (state.isLoading) {
                Box(modifier = Modifier.fillMaxSize()) {
                    CustomCircularProgressIndicator()
                }
            }
        }
    }
}