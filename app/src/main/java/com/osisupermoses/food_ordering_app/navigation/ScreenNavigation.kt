package com.osisupermoses.food_ordering_app.navigation

import androidx.compose.animation.*
import androidx.compose.runtime.Composable
import androidx.navigation.*
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.osisupermoses.food_ordering_app.common.Constants
import com.osisupermoses.food_ordering_app.ui.Login.LoginScreen
import com.osisupermoses.food_ordering_app.ui.cart.CartScreen
import com.osisupermoses.food_ordering_app.ui.list_item.ListItemScreen
import com.osisupermoses.food_ordering_app.ui.checkout.CheckoutScreen
import com.osisupermoses.food_ordering_app.ui.recipes_details.RecipesDetailsScreen
import com.osisupermoses.food_ordering_app.ui.menu.MenuScreen
import com.osisupermoses.food_ordering_app.ui.new_or_existing_user.NewOrExistingScreen
import com.osisupermoses.food_ordering_app.ui.onboarding.WelcomeScreen
import com.osisupermoses.food_ordering_app.ui.sign_up.SignUpScreen

const val NO_BOTTOM_BAR_ROUTE = "bottomBar"

@OptIn(ExperimentalAnimationApi::class, ExperimentalAnimationApi::class)
@Composable
internal fun SetUpNavGraphNoBottomBar(
    navController: NavHostController,
    startDestination: String
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        route = NO_BOTTOM_BAR_ROUTE,
    ) {
        composable(Screens.NoBottomBarScreens.WelcomeScreen.route) {
            WelcomeScreen {
                navController.popBackStack()
                navController.navigate(Screens.NoBottomBarScreens.NewOrExistingScreen.route)
            }
        }
        composable(Screens.NoBottomBarScreens.NewOrExistingScreen.route) {
            NewOrExistingScreen(
                toLogInScreen = {
                    navController.navigate(Screens.NoBottomBarScreens.LogInScreen.route)
                },
                toSignUpScreen = {
                    navController.navigate(Screens.NoBottomBarScreens.SignUpScreen.route)
                }
            )
        }
        composable(Screens.NoBottomBarScreens.SignUpScreen.route) {
            SignUpScreen {
                navController.navigate(Screens.NoBottomBarScreens.MenuScreen.route)
            }
        }
        composable(Screens.NoBottomBarScreens.LogInScreen.route) {
            LoginScreen(
                toNewOrExistingScreen = {
                    navController.navigate(Screens.NoBottomBarScreens.NewOrExistingScreen.route)
                },
                toForgottenPasswordScreen = {  },
                toMenuScreen = {
                    navController.navigate(Screens.NoBottomBarScreens.MenuScreen.route)
                }
            )
        }
        composable(Screens.NoBottomBarScreens.MenuScreen.route) {
            MenuScreen(
                toFoodDetailsScreen = { foodId ->
                    navController.navigate(Screens.NoBottomBarScreens.RecipesDetailsScreen.route
                            + "/$foodId")
                },
                toRestaurantDetailScreen = {
//                    navController.navigate(Screens.NoBottomBarScreens.RecipesDetailsScreen.route
////                    + "/{restaurantId}"
//                    )
                },
                toMenuScreen = { navController.navigate(Screens.NoBottomBarScreens.MenuScreen.route) },
                toAdminScreen = { navController.navigate(Screens.NoBottomBarScreens.ListItemScreen.route) },
                toLogInScreen = { navController.navigate(Screens.NoBottomBarScreens.LogInScreen.route) }
            )
        }
        composable(
            route = Screens.NoBottomBarScreens.ListItemScreen.route
        ) {
            ListItemScreen(
                navigateUp = { navController.navigateUp() },
                goToMenuScreen = { navController.navigate(Screens.NoBottomBarScreens.MenuScreen.route) }
            )
        }
        composable(
            route = Screens.NoBottomBarScreens.RecipesDetailsScreen.route + "/{${Constants.FOOD_ID}}",
            arguments = listOf(
                navArgument(Constants.FOOD_ID) { type = NavType.LongType },
//                navArgument(Constants.RESTAURANT_ID) { type = NavType.IntType},
            )
        ) {
            RecipesDetailsScreen (
                navigateUp = { navController.navigateUp() },
                toCartScreen = { navController.navigate(Screens.NoBottomBarScreens.CartScreen.route) },
                toCheckoutScreen = { price, deliveryFee ->
                    navController.navigate(Screens.NoBottomBarScreens.CheckoutScreen.route +
                            "/${price}/${deliveryFee}"
                    )
                }
            )
        }
        composable(
            route = Screens.NoBottomBarScreens.CartScreen.route
        ) {
            CartScreen(
                navigateUp = { navController.navigate(Screens.NoBottomBarScreens.MenuScreen.route) },
                toItemDetailScreen = { recipeItemId ->
                    navController.navigate(
                        Screens.NoBottomBarScreens.RecipesDetailsScreen.route + "/$recipeItemId"
                    )
                },
                toCheckoutScreen = { subTotal, deliveryFee ->
                    navController.navigate(
                        Screens.NoBottomBarScreens.CheckoutScreen.route +
                                "/${subTotal}/${deliveryFee}"
                    )
                }
            )
        }
        composable(
            route = Screens.NoBottomBarScreens.CheckoutScreen.route +
                    "/{${Constants.ITEM_PRICE}}/{${Constants.DELIVERY_FEE}}",
            arguments = listOf(
                navArgument(Constants.ITEM_PRICE) { type = NavType.StringType },
                navArgument(Constants.DELIVERY_FEE) { type = NavType.StringType},
            )
        ) {
            CheckoutScreen (
                navigateUp = { navController.navigateUp() },
                goToMenuScreen = { navController.navigate(Screens.NoBottomBarScreens.MenuScreen.route) }
            )
        }
    }
}