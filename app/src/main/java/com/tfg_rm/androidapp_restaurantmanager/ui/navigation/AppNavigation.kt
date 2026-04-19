package com.tfg_rm.androidapp_restaurantmanager.ui.navigation

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.core.view.WindowCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.tfg_rm.androidapp_restaurantmanager.R
import com.tfg_rm.androidapp_restaurantmanager.domain.viewmodels.AuthState
import com.tfg_rm.androidapp_restaurantmanager.domain.viewmodels.AuthViewModel
import com.tfg_rm.androidapp_restaurantmanager.domain.viewmodels.EmployeeViewModel
import com.tfg_rm.androidapp_restaurantmanager.domain.viewmodels.FoodViewModel
import com.tfg_rm.androidapp_restaurantmanager.domain.viewmodels.OrdersViewModel
import com.tfg_rm.androidapp_restaurantmanager.domain.viewmodels.TableViewModel
import com.tfg_rm.androidapp_restaurantmanager.ui.screens.DoOrderScreen
import com.tfg_rm.androidapp_restaurantmanager.ui.screens.LoginScreen
import com.tfg_rm.androidapp_restaurantmanager.ui.screens.OrdersScreen
import com.tfg_rm.androidapp_restaurantmanager.ui.screens.ProfileScreen
import com.tfg_rm.androidapp_restaurantmanager.ui.screens.TableScreen

/**
 * Main navigation controller and orchestrator for the application's UI.
 *
 * This Composable manages the [NavHost], defines the routing table, and handles
 * global UI concerns such as the Bottom Navigation Bar, back-button interception,
 * and session expiration events.
 * @param navController The central controller for managing app navigation and the backstack.
 * @see AppScreens
 */
@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
) {
    // ViewModel initialization via Hilt for domain logic access
    val orderViewModel: OrdersViewModel = hiltViewModel()
    val tableViewModel: TableViewModel = hiltViewModel()
    val foodViewModel: FoodViewModel = hiltViewModel()
    val employeeViewModel: EmployeeViewModel = hiltViewModel()
    val authViewModel: AuthViewModel = hiltViewModel()

    // State tracking for current navigation destination
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val context = LocalContext.current
    var showExitDialog by remember { mutableStateOf(false) }
    val view = LocalView.current

    /**
     * Resets the data state of all domain ViewModels.
     * Used during logout or session expiration to ensure no stale data remains.
     */
    val recargarEstado = {
        orderViewModel.resetState()
        tableViewModel.resetState()
        foodViewModel.resetState()
        employeeViewModel.resetState()
    }

    val authState by authViewModel.authState.collectAsState()

    /**
     * Global side-effect to monitor authentication status.
     * If the state changes to [AuthState.LogOut], it clears all local data
     * and forces navigation back to the Login screen.
     */
    LaunchedEffect(authState) {
        if (authState == AuthState.LogOut) {
            recargarEstado()
            navController.navigate(AppScreens.LoginScreen.route) {
                popUpTo(0) // Clear backstack entirely
            }
            Toast.makeText(
                context,
                R.string.loginscreen_loginerror_reloadtoken,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    /**
     * UI Adjustment for system status bar appearance (Light/Dark mode)
     * based on the currently visible screen.
     */
    SideEffect {
        val window = (view.context as Activity).window
        WindowCompat
            .getInsetsController(window, view)
            .isAppearanceLightStatusBars = !(currentRoute in AppScreens.allBottomBarScreens()
                || currentRoute == AppScreens.LoginScreen.route)
    }

    /**
     * Custom BackHandler logic.
     * intercepts the system back button on specific main screens to show
     * a confirmation dialog before exiting the application.
     */
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
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            // Show the BottomBar only if the current route is part of the main tabs
            if (currentRoute in AppScreens.allBottomBarScreens()) {
                BottomBar(navController, currentRoute)
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = AppScreens.LoginScreen.route,
            modifier = Modifier.padding(padding)
        ) {
            // Screen definitions and transitions
            composable(AppScreens.LoginScreen.route) {
                LoginScreen(
                    authViewModel = authViewModel,
                    loginSuccess = { navController.navigate(AppScreens.ProfileScreen.route) },
                    recargarEstados = recargarEstado
                )
            }
            composable(AppScreens.ProfileScreen.route) {
                ProfileScreen(viewModel = employeeViewModel, authViewModel = authViewModel)
            }
            composable(AppScreens.TableScreen.route) {
                TableScreen(
                    viewModel = tableViewModel,
                    goToAddOrders = { navController.navigate(AppScreens.FoodScreen.route) }
                )
            }
            composable(AppScreens.FoodScreen.route) {
                DoOrderScreen(
                    tableViewModel = tableViewModel,
                    backToTables = { navController.popBackStack() },
                    viewModel = foodViewModel
                )
            }
            composable(AppScreens.OrdersScreen.route) {
                OrdersScreen(orderViewModel)
            }
        }

        // Global Application Exit Confirmation Dialog
        if (showExitDialog) {
            AlertDialog(
                onDismissRequest = { showExitDialog = false },
                title = { Text(stringResource(R.string.exitapp)) },
                text = { Text(stringResource(R.string.exitapp_confirmation)) },
                confirmButton = {
                    TextButton(onClick = { (context as Activity).finish() }) {
                        Text(stringResource(R.string.yes))
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showExitDialog = false }) {
                        Text(stringResource(R.string.no))
                    }
                }
            )
        }
    }
}

/**
 * Standard Navigation Bar for the main application modules (Home, Orders, Profile).
 * * @param navController Controller used to switch between tabs.
 * @param currentRoute The active route used to highlight the selected item.
 */
@Composable
fun BottomBar(
    navController: NavHostController,
    currentRoute: String?
) {
    NavigationBar {
        // Home/Table Tab
        NavigationBarItem(
            selected = currentRoute == AppScreens.TableScreen.route,
            onClick = { navController.navigate(AppScreens.TableScreen.route) },
            icon = {
                Icon(
                    if (currentRoute == AppScreens.TableScreen.route) Icons.Default.Home else Icons.Outlined.Home,
                    null, tint = Color.Black
                )
            },
            label = { Text("Home") }
        )

        // Orders Tab
        NavigationBarItem(
            selected = currentRoute == AppScreens.OrdersScreen.route,
            onClick = { navController.navigate(AppScreens.OrdersScreen.route) },
            icon = {
                Icon(Icons.AutoMirrored.Filled.List, null, tint = Color.Black)
            },
            label = { Text("Orders") }
        )

        // Profile Tab
        NavigationBarItem(
            selected = currentRoute == AppScreens.ProfileScreen.route,
            onClick = { navController.navigate(AppScreens.ProfileScreen.route) },
            icon = {
                Icon(
                    if (currentRoute == AppScreens.ProfileScreen.route) Icons.Default.Person else Icons.Outlined.Person,
                    null, tint = Color.Black
                )
            },
            label = { Text("Profile") }
        )
    }
}