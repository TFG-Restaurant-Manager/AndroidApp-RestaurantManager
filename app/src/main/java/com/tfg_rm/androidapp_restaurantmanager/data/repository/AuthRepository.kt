package com.tfg_rm.androidapp_restaurantmanager.data.repository

import com.tfg_rm.androidapp_restaurantmanager.data.remote.datasource.AuthRemoteDataSource
import com.tfg_rm.androidapp_restaurantmanager.data.remote.dto.EmployeeLoginResponse
import com.tfg_rm.androidapp_restaurantmanager.data.remote.network.TokenProvider
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val remote: AuthRemoteDataSource,
    private val tokenProvider: TokenProvider
) {

    suspend fun requestRestaurantsId(
        dni: String,
        password: String
    ) : EmployeeLoginResponse {

        val response = remote.requestRestaurantsId(
            dni = dni,
            password = password
        )
        return response
    }

    suspend fun requestToken(
        dni: String,
        password: String,
        employeeId: Long,
        restaurantId: Long
    ) {

        val response = remote.requestToken(
            dni = dni,
            password = password,
            employeeId = employeeId,
            restaurantId = restaurantId
        )

        tokenProvider.setToken(response.token)
    }
}