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

/**
 * Represents the distinct states of the authentication process.
 */
sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    object LogOut : AuthState()
    data class Error(val msg: Int) : AuthState()
}

/**
 * ViewModel responsible for managing the authentication lifecycle and session state.
 * * It acts as the bridge between the UI and [AuthService], handling user login,
 * logout, and automatic session expiration monitoring. It exposes an [AuthState]
 * to the view to drive the navigation and loading states.
 *
 * @property authService The domain service handling authentication logic.
 */
@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authService: AuthService
) : ViewModel() {

    /**
     * Internal initialization block that subscribes to global session expiration events.
     * If the [SessionManager] signals an expired session, the UI state is automatically
     * shifted to [AuthState.LogOut].
     */
    init {
        viewModelScope.launch {
            SessionManager.sessionExpired.collect {
                Log.e("AuthViewModel", "Session expired or auth changed")
                _authState.value = AuthState.LogOut
            }
        }
    }

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)

    /**
     * Observable state flow representing the current authentication status.
     */
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private var lastCode: String? = null
    private var lastPassword: String? = null

    /**
     * Checks if a valid session token already exists locally.
     * If found, transitions the state directly to [AuthState.Success] to bypass the login screen.
     */
    fun login() {
        viewModelScope.launch {
            try {
                val savedToken = authService.loadToken()
                if (savedToken) {
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
     * Resets the authentication state to [AuthState.Idle].
     * Useful for clearing error messages and allowing the user to try again.
     */
    fun resetState() {
        _authState.value = AuthState.Idle
    }

    /**
     * Performs a login attempt with the provided credentials.
     * * Validates input locally before calling the service. It manages the [AuthState.Loading]
     * state and maps various network or credential exceptions to specific UI string resources.
     *
     * @param code The employee's identification code.
     * @param password The employee's password.
     */
    fun login(code: String, password: String) {
        lastCode = code
        lastPassword = password

        if (code.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error(R.string.loginscreen_loginerror_emptylabels)
        } else {
            _authState.value = AuthState.Loading
            viewModelScope.launch {
                try {
                    authService.requestToken(code = code, password = password)
                    _authState.value = AuthState.Success
                } catch (e: Exception) {
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

    /**
     * Logs the user out by clearing the remote session and updating the UI state
     * to trigger a redirection to the login screen.
     */
    fun logout() {
        _authState.value = AuthState.LogOut
        viewModelScope.launch { authService.logout() }
    }
}