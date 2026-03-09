package com.tfg_rm.androidapp_restaurantmanager.controller

import androidx.navigation.NavController
import com.tfg_rm.androidapp_restaurantmanager.navigation.AppScreens

class TableController {
    fun goToFood (navController: NavController, tableId: Int) {
        navController.navigate(
            AppScreens.FoodScreen.createRoute(tableId)
        )
    }
}