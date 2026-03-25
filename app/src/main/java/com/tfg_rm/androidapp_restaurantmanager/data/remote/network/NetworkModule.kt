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

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideTokenProvider(
        @ApplicationContext context: Context
    ): TokenProvider {
        return TokenProvider(
            context = context
        )
    }

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
                url("https://amy-coordinate-newport-immigration.trycloudflare.com/")

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