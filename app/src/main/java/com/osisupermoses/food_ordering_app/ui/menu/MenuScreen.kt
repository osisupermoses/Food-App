package com.osisupermoses.food_ordering_app.ui.menu

import android.Manifest
import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.osisupermoses.food_ordering_app.R
import com.osisupermoses.food_ordering_app.ui.theme.GoldYellow
import com.osisupermoses.food_ordering_app.ui.theme.spacing
import com.osisupermoses.food_ordering_app.ui.menu.components.*
import com.osisupermoses.food_ordering_app.ui.theme.background
import com.osisupermoses.food_ordering_app.ui.ui_common.SectionHeader
import com.osisupermoses.food_ordering_app.util.loading.CustomCircularProgressIndicator
import kotlinx.coroutines.launch

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MenuScreen(
    viewModel: MenuScreenViewModel = hiltViewModel(),
    toFoodDetailsScreen: (Long) -> Unit,
    toRestaurantDetailScreen: () -> Unit,
    toMenuScreen: () -> Unit,
    toAdminScreen: () -> Unit,
    toLogInScreen: () -> Unit
) {

    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val state = viewModel.state.value
    val context = LocalContext.current
    val activity = (context as? Activity)
    val pressedTime = remember { mutableStateOf<Long>(0) }

    val permissionState = rememberPermissionState(permission = Manifest.permission.READ_EXTERNAL_STORAGE)
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(
        key1 = lifecycleOwner,
        effect = {
            val observer = LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_START) {
                    permissionState.launchPermissionRequest()
                }
            }
            lifecycleOwner.lifecycle.addObserver(observer)

            onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        }
    )

    // Intercepts back navigation when the drawer is open
    if (scaffoldState.drawerState.isOpen) {
        BackHandler {
            scope.launch {
                scaffoldState.drawerState.close()
            }
        }
    }
    // Prevents MainScreen from going back to Login screen on backpress
    BackHandler {
        if (pressedTime.value + 2000 > System.currentTimeMillis()) {
            activity?.finish()
        } else {
            Toast.makeText(
                context,
                context.getString(R.string.please_press_back),
                Toast.LENGTH_SHORT
            ).show()
        }
        pressedTime.value = System.currentTimeMillis()
    }

    // Handles all snackbar activities
    LaunchedEffect(key1 = true) {
        viewModel.errorChannel.collect { error ->
            scaffoldState.snackbarHostState.showSnackbar(
                message = error.asString(context)
            )
        }
    }

    if (permissionState.hasPermission) {
        Scaffold(
            topBar = {
                MenuTopBar(
                    topIcon = painterResource(R.drawable.hamburger_icon),
                    storeAddress = "University of Ibadan, NG",
                    onSearchClick = { }
                ) {
                    scope.launch {
                        scaffoldState.drawerState.open()
                    }
                }
            },
            drawerGesturesEnabled = scaffoldState.drawerState.isOpen,
            drawerBackgroundColor = background.copy(alpha = 0.9f),
            drawerContent = {
                Box(modifier = Modifier.fillMaxSize()) {
                    DrawerHeader(
                        isAdmin = viewModel.isAdmin,
                        accountName = viewModel.accountName
                    )
                    DrawerBody(
                        modifier = Modifier.align(Alignment.CenterStart),
                        items =
                        if (viewModel.isAdmin) {
                            menuItems()
                        } else {
                            menuItems().filter { it.id != "admin" }
                        },
                        onItemClick = { menuItem ->
                            when (menuItem.id) {
                                "admin" -> toAdminScreen.invoke()
                                "home" -> toMenuScreen.invoke()
                                else -> {
                                    Toast.makeText(
                                        context,
                                        context.getString(R.string.this_feature_is_coming_soon),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    )
                    DrawerBottom(modifier = Modifier.align(Alignment.BottomCenter)) {
                        viewModel.signOut(context) { toLogInScreen() }
                    }
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
                            viewModel.foods?.let { foodList ->
                                val listOfFood =
                                    if (permissionState.hasPermission) foodList
                                    else emptyList()
                                itemsIndexed(foodList) { index, food ->
                                    PopularItem(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(MaterialTheme.spacing.small))
                                            .wrapContentHeight(),
                                        image = viewModel.getPopularImageUri(food),
                                        foodName = food.name,
                                        price = food.price!!,
                                        estDeliveryTime = food.estDeliveryTime,
                                        orderRating = food.orderRating!!,
                                        context = context,
                                        onRatingClick = { }
                                    ) { toFoodDetailsScreen.invoke(food.id!!) }
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
                            viewModel.restaurantList?.let { restaurants ->
                                val listOfRestaurants =
                                    if (permissionState.hasPermission) restaurants
                                    else emptyList()
                                itemsIndexed(restaurants) { index, restaurant ->
                                    RestaurantItem(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(MaterialTheme.spacing.small))
                                            .wrapContentHeight(),
                                        frontalImage = viewModel.getRestaurantFrontalImageUri(
                                            restaurant
                                        ),
                                        restaurantName = restaurant.restaurantName!!,
                                        restaurantReviewScore = restaurant.restaurantReviews ?: 0.0,
                                        context = context,
                                        onReviewScoreClick = { }
                                    ) {
//                                    toRestaurantDetailScreen.invoke()
                                        Toast.makeText(
                                            context,
                                            context.getString(R.string.this_feature_is_coming_soon),
                                            Toast.LENGTH_SHORT
                                        ).show()
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
    } else if (viewModel.restaurantList.isEmpty()) {
        val emptyString by remember { mutableStateOf("It is empty here!! Please add some items.") }
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = emptyString,
                style = MaterialTheme.typography.h6.copy(color = Color.LightGray),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth()
            )
        }
    } else Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(id = R.string.go_to_settings_t0),
            style = MaterialTheme.typography.h6.copy(color = Color.LightGray),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
        )
    }
}