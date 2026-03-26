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

class AuthRemoteDataSource @Inject constructor(
    private val client: HttpClient
) {
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