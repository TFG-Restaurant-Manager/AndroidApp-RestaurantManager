package com.tfg_rm.androidapp_restaurantmanager.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.tfg_rm.androidapp_restaurantmanager.domain.viewmodels.TemporalViewModel

@Composable
fun TemporalScreen(
    temporalViewModel: TemporalViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        temporalViewModel.loadUser()
    }
}