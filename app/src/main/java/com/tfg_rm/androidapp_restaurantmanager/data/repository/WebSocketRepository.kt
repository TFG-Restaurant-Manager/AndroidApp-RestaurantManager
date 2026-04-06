package com.tfg_rm.androidapp_restaurantmanager.data.repository

import com.tfg_rm.androidapp_restaurantmanager.data.remote.network.WebSocketManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WebSocketRepository @Inject constructor(
    private val webSocketManager: WebSocketManager
) {
    suspend fun connect() {
        webSocketManager.connect()
    }

    suspend fun disconnect() {
        webSocketManager.disconnect()
    }
}