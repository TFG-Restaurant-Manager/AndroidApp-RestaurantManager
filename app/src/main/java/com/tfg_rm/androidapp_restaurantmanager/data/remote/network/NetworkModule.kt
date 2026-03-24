package com.tfg_rm.androidapp_restaurantmanager.data.remote.network

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
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

            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                    }
                )
            }

            install(WebSockets)

            defaultRequest {
                url("https://across-animal-disks-newspapers.trycloudflare.com/")

                tokenProvider.getToken()?.let {
                    header("Authorization", "Bearer $it")
                }
            }
        }
    }
}