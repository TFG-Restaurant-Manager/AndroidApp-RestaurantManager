package com.tfg_rm.androidapp_restaurantmanager.data.remote.datasource

import com.tfg_rm.androidapp_restaurantmanager.data.remote.dto.OrderResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import javax.inject.Inject

/**
 * Remote DataSource responsible for fetching restaurant order information
 * from the backend.
 *
 * It utilizes a Ktor [HttpClient] to perform HTTP requests to the API.
 *
 * @property client Injected HTTP client used to communicate with the server.
 */
class OrderRemoteDataSource @Inject constructor(
    private val client: HttpClient
) {
    suspend fun getOrders(): List<OrderResponse> =
        client.get("api/order").body()
}