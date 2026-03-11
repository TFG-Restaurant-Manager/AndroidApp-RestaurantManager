package com.tfg_rm.androidapp_restaurantmanager.data.remote.network

class TokenProvider {
    private var token: String? = null

    fun getToken(): String? = token

    fun setToken(newToken: String) {
        token = newToken
    }

    fun clear() {
        token = null
    }
}