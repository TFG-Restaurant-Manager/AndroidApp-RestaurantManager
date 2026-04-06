package com.tfg_rm.androidapp_restaurantmanager.domain.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfg_rm.androidapp_restaurantmanager.R
import com.tfg_rm.androidapp_restaurantmanager.data.remote.network.SessionManager
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
    object LogOut : AuthState()
    data class Error(val msg: Int) : AuthState()
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authService: AuthService
) : ViewModel() {

    init {
        viewModelScope.launch {
            SessionManager.sessionExpired.collect {
                Log.e("AuthViewModel", "Auth cambiado, se supone")
                _authState.value = AuthState.LogOut
            }
        }
    }

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private var lastCode: String? = null
    private var lastPassword: String? = null

    fun login() {
        viewModelScope.launch {
            try {
                val savedToken = authService.loadToken()
                connectWS()
                Log.i("AuthViewModel", "token: ${savedToken ?: "null"}")
                if (savedToken) {
                    // Hay token, podemos considerarlo como login ya hecho
                    _authState.value = AuthState.Success
                } else {
                    _authState.value = AuthState.Idle
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("AuthViewModel", e.message ?: "Error al cargar el usuario guardado")
            }
        }
    }

    /**
     * Method to reset the state of the authentication on error displayed in the ui
     */
    fun resetState() {
        _authState.value = AuthState.Idle
    }

    fun login(
        code: String,
        password: String
    ) {
        lastCode = code
        lastPassword = password

        if (code.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error(R.string.loginscreen_loginerror_emptylabels)
        } else {
            _authState.value = AuthState.Loading
            viewModelScope.launch {
                try {
                    authService.requestToken(code = code, password = password)
                    connectWS()
                    _authState.value = AuthState.Success
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.e("AuthViewModel", e.message ?: "Login failed")
                    _authState.value = AuthState.Error(
                        if (e.message?.contains("JSON input: Invalid credentials") ?: false)
                            R.string.loginscreen_loginerror_invalidcredentials else
                            if (e.message?.contains("Unable to resolve host") ?: false)
                                R.string.loginscreen_loginerror_conexion
                            else R.string.loginscreen_loginerror_common
                    )
                }
            }
        }
    }

    fun logout() {
        _authState.value = AuthState.LogOut
        viewModelScope.launch {
            authService.disconnectWS()
            authService.logout()
        }
    }

    fun connectWS() {
        viewModelScope.launch {
            authService.connectWS()
        }
    }
}