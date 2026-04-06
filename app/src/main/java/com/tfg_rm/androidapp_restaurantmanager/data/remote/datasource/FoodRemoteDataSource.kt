package com.tfg_rm.androidapp_restaurantmanager.data.remote.datasource

import com.tfg_rm.androidapp_restaurantmanager.data.remote.dto.DishesDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import javax.inject.Inject

/**
 * Remote DataSource responsible for fetching dish information from the backend.
 *
 * It utilizes a Ktor [HttpClient] to perform HTTP requests to the API.
 *
 * @property client Injected HTTP client used to communicate with the server.
 */
class FoodRemoteDataSource @Inject constructor(
    private val client: HttpClient
) {

    /**
     * Performs a request to the available dishes endpoint.
     *
     * Fetches the list of dishes from the server and returns them
     * as a list of [DishesDto] objects.
     *
     * @return A list of [DishesDto] containing the dishes' information.
     *
     * @throws io.ktor.client.plugins.ClientRequestException If the request returns a 4xx error.
     * @throws io.ktor.client.plugins.ServerResponseException If the request returns a 5xx error.
     * @throws Exception For any other network or serialization error.
     */
    suspend fun getDishes(): List<DishesDto> {
        val response = client.get("api/dish/info")
        return response.body()
    }
}