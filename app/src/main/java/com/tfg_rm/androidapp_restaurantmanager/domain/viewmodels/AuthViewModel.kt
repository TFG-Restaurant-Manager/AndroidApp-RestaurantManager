package com.tfg_rm.androidapp_restaurantmanager.domain.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfg_rm.androidapp_restaurantmanager.R
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
    object Success : AuthState()
    data class Error(val msg: Int) : AuthState()
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authService: AuthService
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private var lastCode: String? = null
    private var lastPassword: String? = null
    private var lastEmployeeId: Long? = null

    /**
     * Method to reset the state of the authentication on error displayed in the ui
     */
    fun resetState() {
        _authState.value = AuthState.Idle
    }

    fun login(code: String, password: String) {
        lastCode = code
        lastPassword = password
        _authState.value = AuthState.Loading

        if (code.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error(R.string.loginscreen_loginerror_emptylabels)
        } else {
            viewModelScope.launch {
                try {
                    val resp = authService.requestToken(code = code, password = password)

                    _authState.value = AuthState.Success
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.e("AuthViewModel", e.message ?: "Login failed")
                    _authState.value = AuthState.Error(R.string.loginscreen_loginerror_common)
                }
            }
        }
    }
}