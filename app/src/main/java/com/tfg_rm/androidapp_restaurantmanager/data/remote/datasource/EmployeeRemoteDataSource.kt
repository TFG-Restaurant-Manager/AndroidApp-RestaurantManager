package com.tfg_rm.androidapp_restaurantmanager.data.remote.datasource

import com.tfg_rm.androidapp_restaurantmanager.data.remote.dto.EmployeesDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import javax.inject.Inject

/**
 * Remote DataSource responsible for fetching employee information from the backend.
 *
 * It utilizes a Ktor [HttpClient] to perform HTTP requests to the API.
 *
 * @property client Injected HTTP client used to communicate with the server.
 */
class EmployeeRemoteDataSource @Inject constructor(
    private val client: HttpClient
) {

    /**
     * Performs a request to the employee information endpoint.
     *
     * Fetches the authenticated employee's data from the server and returns it
     * as an [EmployeesDto] object.
     *
     * @return [EmployeesDto] containing the employee's information.
     *
     * @throws io.ktor.client.plugins.ClientRequestException If the request returns a 4xx error.
     * @throws io.ktor.client.plugins.ServerResponseException If the request returns a 5xx error.
     * @throws Exception For any other network or serialization error.
     */
    suspend fun getEmployeeData(): EmployeesDto {
        val response = client.get("api/employee/me")
        return response.body()
    }
}