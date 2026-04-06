package com.tfg_rm.androidapp_restaurantmanager.data.repository

import com.tfg_rm.androidapp_restaurantmanager.data.remote.datasource.FoodRemoteDataSource
import com.tfg_rm.androidapp_restaurantmanager.data.remote.mapper.toDishes
import com.tfg_rm.androidapp_restaurantmanager.data.remote.mapper.toOrderDto
import com.tfg_rm.androidapp_restaurantmanager.data.remote.network.TokenProvider
import com.tfg_rm.androidapp_restaurantmanager.data.remote.network.WebSocketManager
import com.tfg_rm.androidapp_restaurantmanager.domain.models.Dishes
import com.tfg_rm.androidapp_restaurantmanager.domain.models.Order
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepositoryFood @Inject constructor(
    private val remote: FoodRemoteDataSource,
    private val tokenProvider: TokenProvider,
    private val webSocketManager: WebSocketManager
) {

    suspend fun getDishes(): List<Dishes> {
        return remote.getDishes().map { it.toDishes() }
    }

    suspend fun saveOrder(order: Order) {
        webSocketManager.sendMessage(Json.encodeToJsonElement(order.toOrderDto()).toString())
    }

    val events = webSocketManager.events

    suspend fun sendUpdate(message: String) {
        webSocketManager.sendMessage(message)
    }
}
