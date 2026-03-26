package com.tfg_rm.androidapp_restaurantmanager.data.remote.network

import kotlinx.coroutines.flow.MutableSharedFlow

object SessionManager {

    private val _sessionExpired = MutableSharedFlow<Unit>()
    val sessionExpired = _sessionExpired

    suspend fun notifySessionExpired() {
        _sessionExpired.emit(Unit)
    }
}