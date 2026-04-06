package com.tfg_rm.androidapp_restaurantmanager.data.remote.network

import android.content.Context
import android.util.Log
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import javax.inject.Singleton

/**
 * Dagger Hilt module responsible for providing network-related dependencies at the application level.
 *
 * This module configures the global [HttpClient] using Ktor, sets up the base URL,
 * handles JSON serialization, and manages authentication headers. It also includes
 * an [HttpResponseValidator] to intercept authorization errors (401/403),
 * clearing the token and notifying the system when a session expires.
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    /**
     * Provides a singleton instance of [TokenProvider].
     *
     * @param context The application context required for the [TokenProvider] to access storage.
     * @return A [TokenProvider] instance for managing authentication tokens.
     */
    @Provides
    @Singleton
    fun provideTokenProvider(
        @ApplicationContext context: Context
    ): TokenProvider {
        return TokenProvider(
            context = context
        )
    }

    /**
     * Provides a singleton instance of the Ktor [HttpClient].
     *
     * The client is configured with:
     * * **OkHttp Engine:** For efficient network operations.
     * * **ContentNegotiation:** Using Kotlinx Serialization, configured to ignore unknown JSON keys.
     * * **WebSockets:** To support real-time communication.
     * * **Default Request:** Sets the base API URL and injects the "Authorization: Bearer" header if a token exists.
     * * **Response Validation:** Intercepts 401 (Unauthorized) and 403 (Forbidden) HTTP status codes
     * to handle session expiration by clearing the stored token.
     *
     * @param tokenProvider The provider used to retrieve and manage the authentication token.
     * @return A fully configured [HttpClient] instance.
     */
    @Provides
    @Singleton
    fun provideHttpClient(
        tokenProvider: TokenProvider
    ): HttpClient {

        return HttpClient(OkHttp) {

            expectSuccess = true

            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                    }
                )
            }

            install(WebSockets)

            defaultRequest {
                url("https://compounds-purchased-waiting-exempt.trycloudflare.com/")

                tokenProvider.getToken()?.let {
                    header("Authorization", "Bearer $it")
                }
            }

            HttpResponseValidator {
                handleResponseExceptionWithRequest { exception, _ ->

                    val clientException = exception as? ClientRequestException
                    Log.e(
                        "NetworkModule",
                        "Excepcion: ${clientException?.message ?: "Error al hacer la peticion http"}"
                    )

                    val status = clientException?.response?.status?.value

                    Log.e("NetworkModule", "STATUS: $status")

                    if (status == 401 || status == 403) {

                        runBlocking {
                            Log.e("NetworkModule", "Ha entrado")
                            tokenProvider.clearToken()
                            SessionManager.notifySessionExpired()
                        }
                    }
                }
            }
        }
    }
}