package com.tfg_rm.androidapp_restaurantmanager.data.remote.datasource

import android.util.Log
import com.tfg_rm.androidapp_restaurantmanager.data.remote.dto.OrderDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
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

    /**
     * Performs a request to the endpoint for orders associated with restaurant tables.
     *
     * In addition to fetching the data, it logs the response status and its content
     * in text format to facilitate debugging.
     *
     * @return A list of [OrderDto] containing the order information.
     *
     * @throws io.ktor.client.plugins.ClientRequestException If the request returns a 4xx error.
     * @throws io.ktor.client.plugins.ServerResponseException If the request returns a 5xx error.
     * @throws Exception For any other network or serialization error.
     */
    suspend fun getOrders(): List<OrderDto> {
        val response = client.get("api/employee/getRestaurantTableOrders")
        Log.i("OrderRemoteDataSource", "Respuesta: ${response.status}")
        Log.i("OrderRemoteDataSource", "Contenido: ${response.bodyAsText()}")
        return response.body()
    }
}