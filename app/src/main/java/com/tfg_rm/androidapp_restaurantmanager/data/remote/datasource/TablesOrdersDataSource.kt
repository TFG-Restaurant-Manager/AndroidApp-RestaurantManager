package com.tfg_rm.androidapp_restaurantmanager.data.remote.datasource

import com.tfg_rm.androidapp_restaurantmanager.data.remote.dto.TablesOrdersDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import javax.inject.Inject

/**
 * Remote DataSource responsible for fetching combined information regarding tables
 * and their associated orders from the backend.
 *
 * It utilizes a Ktor [HttpClient] to perform HTTP requests to the API.
 *
 * @property client Injected HTTP client used to communicate with the server.
 */
class TablesOrdersDataSource @Inject constructor(
    private val client: HttpClient
) {

    /**
     * Performs a request to the endpoint that returns combined information
     * for tables and their orders.
     *
     * Fetches a list of [TablesOrdersDto] objects from the server, where
     * each element contains the data of a table along with its associated orders.
     *
     * @return A list of [TablesOrdersDto] containing table and order information.
     *
     * @throws io.ktor.client.plugins.ClientRequestException If the request returns a 4xx error.
     * @throws io.ktor.client.plugins.ServerResponseException If the request returns a 5xx error.
     * @throws Exception For any other network or serialization error.
     */
    suspend fun getTablesOrders(): List<TablesOrdersDto> {
        return client.get("api/table").body()
    }
}