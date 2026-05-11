package com.tfg_rm.androidapp_restaurantmanager.data.repository

import com.tfg_rm.androidapp_restaurantmanager.data.remote.datasource.OrderRemoteDataSource
import com.tfg_rm.androidapp_restaurantmanager.data.remote.dto.WebsocketMessage
import com.tfg_rm.androidapp_restaurantmanager.data.remote.mapper.toOrder
import com.tfg_rm.androidapp_restaurantmanager.data.remote.mapper.toOrderRequest
import com.tfg_rm.androidapp_restaurantmanager.data.remote.network.SocketManager
import com.tfg_rm.androidapp_restaurantmanager.data.remote.network.TokenProvider
import com.tfg_rm.androidapp_restaurantmanager.domain.models.Order
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.encodeToJsonElement
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository in charge of managing and consolidating order data within the application.
 *
 * This class acts as a high-level data provider that filters and transforms raw table-order
 * information into distinct [Order] domain models. It leverages a combined data source
 * to provide a coherent view of all active orders.
 *
 * @property remote The remote data source for direct order operations.
 * @property tokenProvider The provider responsible for managing authentication tokens.
 */
@Singleton
class RepositoryOrders @Inject constructor(
    private val remote: OrderRemoteDataSource,
    private val tokenProvider: TokenProvider,
    private val socketManager: SocketManager
) {

    /**
     * Retrieves all currently active orders by processing the combined table-order data.
     *
     * It filters for tables that have an active order, groups the results by order ID,
     * and maps the raw data into [Order] domain objects, including status, total, and items.
     *
     * @return A list of [Order] domain objects currently active in the system.
     * @throws Exception If there is an error during data retrieval or date parsing.
     */
    suspend fun getOrders(): List<Order> {
        return remote.getOrders().map { it.toOrder() }
    }

    fun observeMessages() = socketManager.messages

    suspend fun disconnectWS() = socketManager.disconnect()
    suspend fun addOrder(order: Order) {
        val json = Json {
            encodeDefaults = true
        }
        socketManager.sendMessage(
            json.encodeToJsonElement(
                WebsocketMessage(
                    type = "CREATE_ORDER",
                    payload = json.encodeToJsonElement(order.toOrderRequest()) as JsonObject
                )
            ).toString()
        )
    }

    suspend fun updateOrder(order: Order) {
        val json = Json {
            encodeDefaults = true
        }
        socketManager.sendMessage(
            json.encodeToJsonElement(
                WebsocketMessage(
                    type = "UPDATE_ORDER",
                    payload = json.encodeToJsonElement(order.toOrderRequest()) as JsonObject
                )
            ).toString()
        )
    }
}
