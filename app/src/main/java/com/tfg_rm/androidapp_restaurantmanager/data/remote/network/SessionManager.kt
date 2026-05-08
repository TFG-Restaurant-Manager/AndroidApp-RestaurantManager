package com.tfg_rm.androidapp_restaurantmanager.data.remote.network

import com.tfg_rm.androidapp_restaurantmanager.data.remote.network.SessionManager.sessionExpired
import kotlinx.coroutines.flow.MutableSharedFlow

/**
 * Singleton object that manages the application's session state and lifecycle events.
 * * It provides a reactive stream using [MutableSharedFlow] to notify various components
 * of the application when a user's session has expired (e.g., due to a 401/403 HTTP error).
 * * @property sessionExpired A shared flow that emits a signal whenever a session expiration occurs.
 */
object SessionManager {

    /**
     * ES Atributo privado para la sesion del httpclient
     * EN Private atribute for the session of the HttpClient
     */
    private val _sessionExpired = MutableSharedFlow<Unit>()

    /**
     * ES Atributo para la sesion del httpclient
     * EN Atribute for the session of the HttpClient
     */
    val sessionExpired = _sessionExpired

    /**
     * Triggers a session expiration event.
     * * This method emits a signal through the [sessionExpired] flow, allowing registered
     * observers (such as ViewModels or Navigation handlers) to react, typically by
     * redirecting the user to the login screen.
     */
    suspend fun notifySessionExpired() {
        _sessionExpired.emit(Unit)
    }
}