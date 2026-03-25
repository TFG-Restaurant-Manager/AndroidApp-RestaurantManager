package com.tfg_rm.androidapp_restaurantmanager.data.remote.network

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

private val Context.dataStore by preferencesDataStore("auth_prefs")

class TokenProvider(
    private val context: Context
) {

    private val TOKEN_KEY = stringPreferencesKey("token")

    private var token: String? = null

    suspend fun loadToken(): Boolean {
        val prefs = context.dataStore.data.first()
        token = prefs[TOKEN_KEY]
        return !token.isNullOrEmpty()
    }

    fun getToken(): String? {
        return token
    }

    suspend fun setToken(newToken: String) {
        token = newToken

        context.dataStore.edit { prefs ->
            prefs[TOKEN_KEY] = newToken
        }
    }

    suspend fun clearToken() {
        token = null

        context.dataStore.edit { prefs ->
            prefs.remove(TOKEN_KEY)
        }
    }
}