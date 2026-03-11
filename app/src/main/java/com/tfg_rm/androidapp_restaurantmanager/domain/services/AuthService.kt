package com.tfg_rm.androidapp_restaurantmanager.domain.services

import com.tfg_rm.androidapp_restaurantmanager.data.repository.AuthRepository
import javax.inject.Inject

class AuthService @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend fun requestToken(
        dni: String,
        password: String,
        employeeId: Long,
        restaurantId: Long
    ) = authRepository.requestToken(
        dni = dni,
        password = password,
        employeeId = employeeId,
        restaurantId = restaurantId
    )

    suspend fun requestRestaurantsId(
        dni: String,
        password: String
    ) = authRepository.requestRestaurantsId(
        dni = dni,
        password = password
    )
}