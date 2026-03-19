package com.tfg_rm.androidapp_restaurantmanager.data.repository

import com.tfg_rm.androidapp_restaurantmanager.data.remote.datasource.AuthRemoteDataSource
import com.tfg_rm.androidapp_restaurantmanager.data.remote.network.TokenProvider
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val remote: AuthRemoteDataSource,
    private val tokenProvider: TokenProvider
) {
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
}