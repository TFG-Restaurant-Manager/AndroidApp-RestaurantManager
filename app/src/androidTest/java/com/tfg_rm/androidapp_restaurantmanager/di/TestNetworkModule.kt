package com.tfg_rm.androidapp_restaurantmanager.di

import android.content.Context
import com.tfg_rm.androidapp_restaurantmanager.data.remote.network.NetworkModule
import com.tfg_rm.androidapp_restaurantmanager.data.remote.network.SocketManager
import com.tfg_rm.androidapp_restaurantmanager.data.remote.network.TokenProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import io.ktor.utils.io.ByteReadChannel
import kotlinx.serialization.json.Json
import javax.inject.Singleton

/**
 * Replaces [NetworkModule] during instrumented tests.
 *
 * Provides a [MockEngine]-backed [HttpClient] so no real network calls are made.
 * The mock engine returns an empty 200 response for any request, preventing
 * accidental connections to the real backend during tests.
 */
@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [NetworkModule::class]
)
object TestNetworkModule {

    @Provides
    @Singleton
    fun provideTokenProvider(
        @ApplicationContext context: Context
    ): TokenProvider = TokenProvider(context)

    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient = HttpClient(
        MockEngine { _ ->
            respond(
                content = ByteReadChannel("{}"),
                status = HttpStatusCode.OK,
                headers = headersOf(
                    HttpHeaders.ContentType,
                    ContentType.Application.Json.toString()
                )
            )
        }
    ) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
        install(WebSockets)
        defaultRequest { url("https://test-host/") }
    }

    @Provides
    @Singleton
    fun provideSocketManager(client: HttpClient): SocketManager = SocketManager(client)
}
