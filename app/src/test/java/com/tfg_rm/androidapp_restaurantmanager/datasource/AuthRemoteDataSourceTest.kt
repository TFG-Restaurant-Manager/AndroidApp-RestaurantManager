package com.tfg_rm.androidapp_restaurantmanager.datasource

import com.tfg_rm.androidapp_restaurantmanager.data.remote.datasource.AuthRemoteDataSource
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondError
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.test.assertFailsWith

class AuthRemoteDataSourceTest {

    private fun buildClient(engine: MockEngine): HttpClient = HttpClient(engine) {
        expectSuccess = true
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
        defaultRequest {
            url("https://test.local/")
        }
    }

    @Test
    fun `requestToken exitoso retorna EmployeeTokenResponse con el token`() = runTest {
        val mockEngine = MockEngine {
            respond(
                content = ByteReadChannel("""{"token":"test-token-123"}"""),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val dataSource = AuthRemoteDataSource(buildClient(mockEngine))
        val result = dataSource.requestToken("EMP001", "secret")

        assertEquals("test-token-123", result.token)
    }

    @Test
    fun `requestToken con credenciales invalidas lanza ClientRequestException`() = runTest {
        val mockEngine = MockEngine {
            respondError(HttpStatusCode.Unauthorized)
        }

        val dataSource = AuthRemoteDataSource(buildClient(mockEngine))

        assertFailsWith<ClientRequestException> {
            dataSource.requestToken("EMP001", "wrongpass")
        }
    }

    @Test
    fun `requestToken envia la peticion al endpoint correcto`() = runTest {
        var capturedPath = ""
        val mockEngine = MockEngine { request ->
            capturedPath = request.url.encodedPath
            respond(
                content = ByteReadChannel("""{"token":"tok"}"""),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val dataSource = AuthRemoteDataSource(buildClient(mockEngine))
        dataSource.requestToken("EMP001", "pass")

        assertEquals("/api/auth/employeeLogin", capturedPath)
    }
}
