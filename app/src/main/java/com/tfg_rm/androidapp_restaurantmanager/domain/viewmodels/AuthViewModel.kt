package com.tfg_rm.androidapp_restaurantmanager.domain.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfg_rm.androidapp_restaurantmanager.domain.services.AuthService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class ChooseRestaurant(val employeeId: Long, val restaurantIds: List<Long>) : AuthState()
    object LoggedIn : AuthState()
    data class Error(val msg: String) : AuthState()
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authService: AuthService
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private var lastDni: String? = null
    private var lastPassword: String? = null
    private var lastEmployeeId: Long? = null

    fun login(dni: String, password: String) {
        lastDni = dni
        lastPassword = password
        _authState.value = AuthState.Loading

        viewModelScope.launch {
            try {
                val resp = authService.requestRestaurantsId(dni = dni, password = password)
                lastEmployeeId = resp.employeeId
                _authState.value = AuthState.ChooseRestaurant(
                    employeeId = resp.employeeId,
                    restaurantIds = resp.restaurantIds
                )
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Login failed")
            }
        }
    }

    fun selectRestaurant(restaurantId: Long) {
        val dni = lastDni
        val password = lastPassword
        val employeeId = lastEmployeeId

        if (dni == null || password == null || employeeId == null) {
            _authState.value = AuthState.Error("Missing credentials")
            return
        }

        _authState.value = AuthState.Loading
        viewModelScope.launch {
            try {
                authService.requestToken(
                    dni = dni,
                    password = password,
                    employeeId = employeeId,
                    restaurantId = restaurantId
                )
                _authState.value = AuthState.LoggedIn
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Token request failed")
            }
        }
    }
}