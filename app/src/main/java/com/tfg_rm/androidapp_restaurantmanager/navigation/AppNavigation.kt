package com.tfg_rm.androidapp_restaurantmanager.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tfg_rm.androidapp_restaurantmanager.ui.screens.LoginScreen
import com.tfg_rm.androidapp_restaurantmanager.ui.screens.OrdersScreen
import com.tfg_rm.androidapp_restaurantmanager.viewmodels.OrdersViewModel

/**
 * Funcion Composable para mostrar todas las pantallas de la aplicacion
 * Este metodo funciona como un navegador entre pantallas
 *
 * Este metodo inicializa la pantalla de Login mediante su ruta
 * Las rutas son guardadas en AppScreens para una mayor comodidad
 * @see AppScreens
 *
 * @param navController, lo inicializa con un rememberNavController,
 * para poder recordad por las pantallas que va pasando
 *
 */
@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    orderViewModel: OrdersViewModel = OrdersViewModel()
) {
    NavHost(
        navController = navController,
        startDestination = AppScreens.LoginScreen.route
    ) {
        composable(AppScreens.LoginScreen.route) {
            LoginScreen()
        }
        composable (AppScreens.OrdersScreen.route) {
            OrdersScreen(orderViewModel)
        }
    }
}

