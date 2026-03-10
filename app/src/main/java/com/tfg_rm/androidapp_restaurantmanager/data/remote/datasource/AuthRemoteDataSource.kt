package com.tfg_rm.androidapp_restaurantmanager.data.remote.datasource

import com.tfg_rm.androidapp_restaurantmanager.data.remote.dto.EmployeeLoginRequest
import com.tfg_rm.androidapp_restaurantmanager.data.remote.dto.EmployeeLoginResponse
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

    suspend fun requestRestaurantsId(
        dni: String,
        password: String
    ): EmployeeLoginResponse {

        return client.post("/auth/employeeLogin") {

            contentType(ContentType.Application.Json)

            setBody(
                EmployeeLoginRequest(
                    dni = dni,
                    password = password
                )
            )
        }.body()
    }

    suspend fun requestToken(
        dni: String,
        password: String,
        employeeId: Long,
        restaurantId: Long
    ): EmployeeTokenResponse {

        return client.post("/auth/employeeToken") {

            contentType(ContentType.Application.Json)

            setBody(
                EmployeeTokenRequest(
                    dni = dni,
                    password = password,
                    employeeId = employeeId,
                    restaurantId = restaurantId,
                )
            )
        }.body()
    }
}