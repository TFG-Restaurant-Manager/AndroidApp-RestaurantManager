package com.tfg_rm.androidapp_restaurantmanager.navigation


/**
 * Clase de tipo sealed para poder crear una clase que guarde clases
 * En esta clase se guardan clases heredadas de AppScreen
 * con una ruta para poder navegar con facilidad utilizando AppNavigation
 * @see AppNavigation
 */
sealed class AppScreens (val route : String){
    object LoginScreen : AppScreens("login_screen")
    object ProfileScreen : AppScreens("profile_screen")
    object TableScreen : AppScreens("table_screen")
    object FoodScreen : AppScreens("food/{tableId}") {
        fun createRoute(tableId: Int) = "food/$tableId"
    }

    // companion object es un objeto que pertenece a la clase y no a una instancia concreta
    // Es como una funcion estatica
    companion object {
        // Devuelve todas las rutas que tienen BottomBar
        fun allBottomBarScreens(): List<String> {
            return listOf(
                ProfileScreen.route,
                TableScreen.route,
                FoodScreen.route)
        }
    }
}