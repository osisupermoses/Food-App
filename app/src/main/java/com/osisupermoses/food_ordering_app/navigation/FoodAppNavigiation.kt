package com.osisupermoses.food_ordering_app.navigation

sealed class Screens(val route: String, var title: String) {

    sealed class NoBottomBarScreens(route: String, title: String = "") : Screens(route, title) {
        object WelcomeScreen : NoBottomBarScreens("welcome")
        object NewOrExistingScreen : NoBottomBarScreens("new_or_existing")
        object SignUpScreen : NoBottomBarScreens("signup")
        object LogInScreen : NoBottomBarScreens("login")
        object MenuScreen : NoBottomBarScreens("menu")
        object RecipesDetailsScreen : NoBottomBarScreens("recipes_details")
        object CartScreen : NoBottomBarScreens("cart")
        object AddPaymentCardScreen : NoBottomBarScreens("add_payment_card")
        object CheckoutScreen : NoBottomBarScreens("checkout")
        object ListItemScreen : NoBottomBarScreens("list_item")
    }

    sealed class BottomBarScreens(route: String, title: String = "") : Screens(route, title) {}

    sealed class LeafScreens(
        route: String,
        title: String = "",
    ) : Screens(route, title) {
        fun createRoute(root: BottomBarScreens) = "${root.route}/$route"

//        object WelcomeScreen : LeafScreens(route = "welcome")
//        object DashboardOrHomeScreen : LeafScreens(route = "dashboard", title = "Dashboard")
//        object ManualInputScreen : LeafScreens(
//            route = "home_manual_input/{transactionType}", title = "Enter Amount"
//        ) {
//            fun createRoute(root: BottomBarScreens, transactionType: String): String {
//                return "${root.route}/home_manual_input/$transactionType"
//            }
//        }
    }
}