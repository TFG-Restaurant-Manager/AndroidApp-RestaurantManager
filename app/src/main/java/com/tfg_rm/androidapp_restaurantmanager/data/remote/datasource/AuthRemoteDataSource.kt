package com.tfg_rm.androidapp_restaurantmanager.data.remote.datasource

import com.tfg_rm.androidapp_restaurantmanager.data.remote.dto.EmployeeTokenRequest
import com.tfg_rm.androidapp_restaurantmanager.data.remote.dto.EmployeeTokenResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import javax.inject.Inject

/**
 * Remote DataSource responsible for managing authentication operations
 * against the backend.
 *
 * It utilizes a Ktor [HttpClient] to perform the necessary HTTP requests
 * to obtain an employee's authentication token.
 *
 * @property client Injected HTTP client used to perform the API requests.
 */
class AuthRemoteDataSource @Inject constructor(
    private val client: HttpClient
) {

    /**
     * Performs a request to the employee login endpoint to obtain an authentication token.
     *
     * Sends the user credentials (code and password) in the request body
     * in JSON format and expects an [EmployeeTokenResponse] object as a result.
     *
     * @param code The employee's identification code.
     * @param password The employee's password.
     *
     * @return [EmployeeTokenResponse] containing the authentication token.
     *
     * @throws io.ktor.client.plugins.ClientRequestException If the request returns a 4xx error.
     * @throws io.ktor.client.plugins.ServerResponseException If the request returns a 5xx error.
     * @throws Exception for any other network or serialization error.
     */
    suspend fun requestToken(
        code: String,
        password: String
    ): EmployeeTokenResponse {

        return client.post("api/auth/employeeLogin") {

            contentType(ContentType.Application.Json)

            setBody(
                EmployeeTokenRequest(
                    code = code,
                    password = password
                )
            )
        }.body()
    }
}