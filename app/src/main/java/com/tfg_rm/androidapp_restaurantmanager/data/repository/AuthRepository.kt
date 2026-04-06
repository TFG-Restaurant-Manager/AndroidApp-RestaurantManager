package com.tfg_rm.androidapp_restaurantmanager.data.repository

import com.tfg_rm.androidapp_restaurantmanager.data.remote.datasource.AuthRemoteDataSource
import com.tfg_rm.androidapp_restaurantmanager.data.remote.network.TokenProvider
import javax.inject.Inject

/**
 * Repository in charge of managing authentication logic and token persistence.
 * * This class serves as an intermediary between the remote data source and the
 * [TokenProvider], ensuring that authentication tokens are properly requested,
 * stored, and cleared across the application.
 *
 * @property remote The remote data source used to perform authentication requests.
 * @property tokenProvider The provider responsible for managing the token's lifecycle in storage and memory.
 */
class AuthRepository @Inject constructor(
    private val remote: AuthRemoteDataSource,
    private val tokenProvider: TokenProvider
) {

    /**
     * Requests a new authentication token from the server and persists it locally.
     *
     * @param code The unique verification code provided to the employee.
     * @param password The security password required for authorization.
     * @throws Exception If the network request fails or the credentials are invalid.
     */
    suspend fun requestToken(
        code: String,
        password: String
    ) {

        val response = remote.requestToken(
            code = code,
            password = password,
        )

        tokenProvider.setToken(response.token)
    }

    /**
     * Performs the logout operation by removing the authentication token from the system.
     */
    suspend fun logout() {
        tokenProvider.clearToken()
    }

    /**
     * Attempts to load an existing token from persistent storage into the memory cache.
     *
     * @return True if a valid token was found and successfully loaded, false otherwise.
     */
    suspend fun loadToken(): Boolean {
        return tokenProvider.loadToken()
    }
}