package com.tfg_rm.androidapp_restaurantmanager.ui.navigation

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.tfg_rm.androidapp_restaurantmanager.R
import com.tfg_rm.androidapp_restaurantmanager.domain.viewmodels.FoodViewModel
import com.tfg_rm.androidapp_restaurantmanager.domain.viewmodels.OrdersViewModel
import com.tfg_rm.androidapp_restaurantmanager.domain.viewmodels.TableViewModel
import com.tfg_rm.androidapp_restaurantmanager.ui.screens.DoOrderScreen
import com.tfg_rm.androidapp_restaurantmanager.ui.screens.LoginScreen
import com.tfg_rm.androidapp_restaurantmanager.ui.screens.OrdersScreen
import com.tfg_rm.androidapp_restaurantmanager.ui.screens.ProfileScreen
import com.tfg_rm.androidapp_restaurantmanager.ui.screens.TableScreen

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
) {
    val orderViewModel: OrdersViewModel = hiltViewModel()
    val tableViewModel: TableViewModel = hiltViewModel()
    val foodViewModel: FoodViewModel = hiltViewModel()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val context = LocalContext.current
    var showExitDialog by remember { mutableStateOf(false) }

    val shouldInterceptBack = currentRoute in listOf(
        AppScreens.LoginScreen.route,
        AppScreens.ProfileScreen.route,
        AppScreens.TableScreen.route,
        AppScreens.OrdersScreen.route
    )

    if (shouldInterceptBack) {
        BackHandler {
            showExitDialog = true
        }
    }

    Scaffold(
        bottomBar = {
            if (currentRoute in AppScreens.allBottomBarScreens()) {
                BottomBar(navController)
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = AppScreens.LoginScreen.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(AppScreens.LoginScreen.route) {
                LoginScreen(
                    loginSuccess = { navController.navigate(AppScreens.ProfileScreen.route) }
                )
            }
            composable(AppScreens.ProfileScreen.route) {
                ProfileScreen(
                    BackToLogin = { navController.navigate(AppScreens.LoginScreen.route) }
                )
            }

            // ESTO DABA ERROR EN EL MERGE
            composable(AppScreens.TableScreen.route) {
                TableScreen(
                    controller = tableViewModel,
                    goToAddOrders = { navController.navigate(AppScreens.FoodScreen.route) }
                )
            }
            composable(AppScreens.FoodScreen.route) {
                DoOrderScreen(
                    tableViewModel = tableViewModel,
                    BackToTables = { navController.popBackStack() },
                    viewModel = foodViewModel
                )
            }
            composable(AppScreens.OrdersScreen.route) {
                OrdersScreen(orderViewModel)
            }

        }
        if (showExitDialog) {
            AlertDialog(
                onDismissRequest = { showExitDialog = false },
                title = { Text(stringResource(R.string.exitapp)) },
                text = { Text(stringResource(R.string.exitapp_confirmation)) },
                confirmButton = {
                    TextButton(onClick = {
                        (context as Activity).finish()
                    }) {
                        Text(stringResource(R.string.yes))
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        showExitDialog = false
                    }) {
                        Text(stringResource(R.string.no))
                    }
                }
            )
        }
    }
}

@Composable
fun BottomBar(navController: NavHostController) {
    NavigationBar {
        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate(AppScreens.TableScreen.route) },
            icon = { Icon(Icons.Default.Home, null) },
            label = { Text("Home") }
        )

        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate(AppScreens.OrdersScreen.route) },
            icon = { Icon(Icons.AutoMirrored.Filled.List, null) },
            label = { Text("Orders") }
        )

        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate(AppScreens.ProfileScreen.route) },
            icon = { Icon(Icons.Default.Person, null) },
            label = { Text("Profile") }
        )
    }
}