package com.tfg_rm.androidapp_restaurantmanager.data.remote.network

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

private val Context.dataStore by preferencesDataStore("auth_prefs")

/**
 * Service responsible for managing the persistence and memory caching of the authentication token.
 * * It uses Android DataStore for secure, asynchronous storage of the token and maintains
 * a local copy in memory for fast access during network requests.
 *
 * @property context The Android [Context] used to access the DataStore.
 */
class TokenProvider(
    private val context: Context
) {

    /** * The preference key used to store and retrieve the token string in DataStore.
     */
    private val TOKEN_KEY = stringPreferencesKey("token")

    /** * In-memory cache of the current authentication token to avoid repeated disk reads.
     */
    private var token: String? = null

    /**
     * Reads the token from the persistent DataStore and updates the in-memory cache.
     *
     * @return True if a valid (non-empty) token was found and loaded, false otherwise.
     */
    suspend fun loadToken(): Boolean {
        val prefs = context.dataStore.data.first()
        token = prefs[TOKEN_KEY]
        return !token.isNullOrEmpty()
    }

    /**
     * Returns the currently cached authentication token.
     *
     * @return The token string or null if no token is loaded in memory.
     */
    fun getToken(): String? {
        return token
    }

    /**
     * Saves a new token both in the in-memory cache and in the persistent DataStore.
     *
     * @param newToken The new authentication token to be stored.
     */
    suspend fun setToken(newToken: String) {
        token = newToken

        context.dataStore.edit { prefs ->
            prefs[TOKEN_KEY] = newToken
        }
    }

    /**
     * Removes the token from both the in-memory cache and the persistent DataStore.
     * * This is typically used during logout or when a session expires.
     */
    suspend fun clearToken() {
        token = null

        context.dataStore.edit { prefs ->
            prefs.remove(TOKEN_KEY)
        }
    }
}