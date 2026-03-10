package com.tfg_rm.androidapp_restaurantmanager.domain.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfg_rm.androidapp_restaurantmanager.domain.services.ServicioTemporal
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TemporalViewModel @Inject constructor(
    private val serviceTemporal: ServicioTemporal
) : ViewModel() {

    fun loadUser() {
        viewModelScope.launch {
            val user = serviceTemporal.getUserName()
            println(user)
        }
    }
}