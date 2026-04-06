package com.tfg_rm.androidapp_restaurantmanager.ui.navigation


/**
 * Sealed class defining the navigation routes for the application.
 *
 * This structure centralizes all route identifiers, preventing hardcoded string errors
 * and facilitating the implementation of the [NavHost] in [AppNavigation].
 * @property route The string identifier used by the Navigation component to resolve destinations.
 * @see AppNavigation
 */
sealed class AppScreens(val route: String) {

    /** Route for the initial authentication screen. */
    object LoginScreen : AppScreens("login_screen")

    /** Route for the active orders management screen. */
    object OrdersScreen : AppScreens("orders_screen")

    /** Route for the employee profile and schedule screen. */
    object ProfileScreen : AppScreens("profile_screen")

    /** Route for the restaurant floor plan and table selection screen. */
    object TableScreen : AppScreens("table_screen")

    /** * Route for the food ordering screen.
     * Includes a dynamic parameter {tableId} to contextually link the order to a specific table.
     */
    object FoodScreen : AppScreens("food/{tableId}") {
        /**
         * Generates a formatted route string with a specific table ID.
         * @param tableId The ID of the table to be passed as an argument.
         */
        fun createRoute(tableId: Int) = "food/$tableId"
    }

    companion object {
        /**
         * Provides a list of routes that should display the [BottomBar].
         * * @return A list of strings containing the routes for Profile, Table, and Orders.
         */
        fun allBottomBarScreens(): List<String> {
            return listOf(
                ProfileScreen.route,
                TableScreen.route,
                OrdersScreen.route
            )
        }
    }
}