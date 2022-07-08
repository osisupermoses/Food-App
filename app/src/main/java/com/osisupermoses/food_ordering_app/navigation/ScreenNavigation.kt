package com.osisupermoses.food_ordering_app.navigation

import androidx.compose.animation.*
import androidx.compose.runtime.Composable
import androidx.navigation.*
import androidx.navigation.NavDestination.Companion.hierarchy
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.osisupermoses.food_ordering_app.common.Constants
import com.osisupermoses.food_ordering_app.ui.Login.LoginScreen
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
    startDestination: String = Screens.NoBottomBarScreens.MenuScreen.route
) {
    AnimatedNavHost(
        navController = navController,
        startDestination = startDestination,
        route = NO_BOTTOM_BAR_ROUTE,
        enterTransition = { defaultFoodAppEnterTransition(initialState, targetState) },
        exitTransition = { defaultFoodAppExitTransition(initialState, targetState) },
        popEnterTransition = { defaultFoodAppPopEnterTransition() },
        popExitTransition = { defaultFoodAppPopExitTransition() }
    ) {
        composable(Screens.NoBottomBarScreens.WelcomeScreen.route) {
            WelcomeScreen {
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
                }
            )
        }
        composable(
            route = Screens.NoBottomBarScreens.RecipesDetailsScreen.route + "/{${Constants.FOOD_ID}}",
            arguments = listOf(
                navArgument(Constants.FOOD_ID) { type = NavType.IntType },
//                navArgument(Constants.RESTAURANT_ID) { type = NavType.IntType},
            )
        ) {
            RecipesDetailsScreen (
                navigateUp = { navController.navigateUp() },
                toCheckoutScreen = { price, deliveryFee ->
                    navController.navigate(Screens.NoBottomBarScreens.CheckoutScreen.route +
                            "/${price}/${deliveryFee}"
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
            CheckoutScreen { navController.navigateUp() }
        }
    }
}

@ExperimentalAnimationApi
private fun AnimatedContentScope<*>.defaultFoodAppEnterTransition(
    initial: NavBackStackEntry,
    target: NavBackStackEntry,
): EnterTransition {
    val initialNavGraph = initial.destination.hostNavGraph
    val targetNavGraph = target.destination.hostNavGraph
    // If we're crossing nav graphs (bottom navigation graphs), we crossfade
    if (initialNavGraph.id != targetNavGraph.id) {
        return fadeIn()
    }
    // Otherwise we're in the same nav graph, we can imply a direction
    return fadeIn() + slideIntoContainer(AnimatedContentScope.SlideDirection.Start)
}

@ExperimentalAnimationApi
private fun AnimatedContentScope<*>.defaultFoodAppExitTransition(
    initial: NavBackStackEntry,
    target: NavBackStackEntry,
): ExitTransition {
    val initialNavGraph = initial.destination.hostNavGraph
    val targetNavGraph = target.destination.hostNavGraph
    // If we're crossing nav graphs (bottom navigation graphs), we crossfade
    if (initialNavGraph.id != targetNavGraph.id) {
        return fadeOut()
    }
    // Otherwise we're in the same nav graph, we can imply a direction
    return fadeOut() + slideOutOfContainer(AnimatedContentScope.SlideDirection.Start)
}

private val NavDestination.hostNavGraph: NavGraph
    get() = hierarchy.first { it is NavGraph } as NavGraph

@ExperimentalAnimationApi
private fun AnimatedContentScope<*>.defaultFoodAppPopEnterTransition(): EnterTransition {
    return fadeIn() + slideIntoContainer(AnimatedContentScope.SlideDirection.End)
}

@ExperimentalAnimationApi
private fun AnimatedContentScope<*>.defaultFoodAppPopExitTransition(): ExitTransition {
    return fadeOut() + slideOutOfContainer(AnimatedContentScope.SlideDirection.End)
}