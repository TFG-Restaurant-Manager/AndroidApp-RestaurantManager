package com.tfg_rm.androidapp_restaurantmanager.domain.services

import com.tfg_rm.androidapp_restaurantmanager.data.repository.AuthRepository
import javax.inject.Inject

class AuthService @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend fun requestToken(
        code: String,
        password: String
    ) = authRepository.requestToken(
        code = code,
        password = password
    )
}