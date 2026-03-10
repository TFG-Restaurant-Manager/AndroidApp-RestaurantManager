package com.tfg_rm.androidapp_restaurantmanager.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.tfg_rm.androidapp_restaurantmanager.domain.viewmodels.AuthViewModel

@Composable
fun TemporalScreen(
    authViewModel: AuthViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        authViewModel.loadRestaurants("12345678", "")
    }
}