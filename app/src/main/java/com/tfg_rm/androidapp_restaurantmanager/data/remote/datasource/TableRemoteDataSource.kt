package com.tfg_rm.androidapp_restaurantmanager.data.remote.datasource

import com.tfg_rm.androidapp_restaurantmanager.data.remote.dto.TablesDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import javax.inject.Inject

/**
 * Remote DataSource responsible for fetching restaurant table and section information
 * from the backend.
 *
 * It utilizes a Ktor [HttpClient] to perform HTTP requests to the API.
 *
 * @property client Injected HTTP client used to communicate with the server.
 */
class TableRemoteDataSource @Inject constructor(
    private val client: HttpClient
) {
    /**
     * Performs a request to the restaurant tables endpoint.
     *
     * Fetches the list of available tables from the server and returns them
     * as a list of [TablesDto] objects.
     *
     * @return A list of [TablesDto] containing table information.
     *
     * @throws io.ktor.client.plugins.ClientRequestException If the request returns a 4xx error.
     * @throws io.ktor.client.plugins.ServerResponseException If the request returns a 5xx error.
     * @throws Exception For any other network or serialization error.
     */
    suspend fun getTables(): List<TablesDto> {
        return client.get("").body()
    }

    /**
     * Performs a request to the restaurant sections endpoint.
     *
     * Fetches the different sections (zones) of the restaurant from the server
     * and returns them as a list of Strings.
     *
     * @return A list of section names.
     *
     * @throws io.ktor.client.plugins.ClientRequestException If the request returns a 4xx error.
     * @throws io.ktor.client.plugins.ServerResponseException If the request returns a 5xx error.
     * @throws Exception For any other network or serialization error.
     */
    suspend fun getSections(): List<String> {
        return client.get("").body()
    }
}