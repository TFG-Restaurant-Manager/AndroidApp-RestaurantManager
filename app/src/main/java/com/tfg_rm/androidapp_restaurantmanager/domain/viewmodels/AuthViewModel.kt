package com.tfg_rm.androidapp_restaurantmanager.domain.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfg_rm.androidapp_restaurantmanager.domain.services.AuthService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authService: AuthService
) : ViewModel() {

    fun loadRestaurants(
        dni: String,
        password: String
    ) {
        viewModelScope.launch {
            authService.requestRestaurantsId(
                dni = dni,
                password = "1234"
            ).restaurantIds.forEach {
                println(it)
            }
        }
    }

    fun loadToken() {
        viewModelScope.launch {
            authService.requestToken(
                dni = "12345678",
                password = "1234",
                employeeId = 1,
                restaurantId = 1
            )
        }
    }
}