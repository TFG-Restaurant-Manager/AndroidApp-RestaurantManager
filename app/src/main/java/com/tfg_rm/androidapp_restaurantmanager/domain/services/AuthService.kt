package com.tfg_rm.androidapp_restaurantmanager.domain.services

import com.tfg_rm.androidapp_restaurantmanager.data.repository.AuthRepository
import javax.inject.Inject

/**
 * Domain service that acts as an abstraction layer for authentication operations.
 * * This service coordinates with the [AuthRepository] to manage the user's session,
 * shielding the higher layers (like ViewModels) from the direct data implementation
 * details.
 *
 * @property authRepository The repository handling the low-level authentication and token logic.
 */
class AuthService @Inject constructor(
    private val authRepository: AuthRepository
) {
    /**
     * Authenticates the user using a code and password, obtaining a session token.
     *
     * @param code The unique identifier for the employee.
     * @param password The security credentials.
     */
    suspend fun requestToken(
        code: String,
        password: String
    ) = authRepository.requestToken(
        code = code,
        password = password
    )

    /**
     * Terminates the current session by clearing stored credentials.
     */
    suspend fun logout() {
        authRepository.logout()
    }

    /**
     * Verifies if there is a valid token available in the system.
     *
     * @return True if a token exists and is loaded, false otherwise.
     */
    suspend fun loadToken(): Boolean {
        return authRepository.loadToken()
    }
}