package com.tfg_rm.androidapp_restaurantmanager.datasource

import com.tfg_rm.androidapp_restaurantmanager.data.remote.datasource.AuthRemoteDataSource
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import io.ktor.client.engine.mock.toByteArray
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Test

/**
 * Unit tests for [AuthRemoteDataSource] using Ktor's [MockEngine].
 *
 * Covers:
 * - 200 response returns a populated [EmployeeTokenResponse]
 * - 401 response throws [ClientRequestException]
 * - 500 response throws [ServerResponseException]
 */
class AuthRemoteDataSourceTest {

    private fun buildClient(engine: MockEngine): HttpClient = HttpClient(engine) {
        expectSuccess = true
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
        defaultRequest {
            url("https://test-host/")
        }
    }

    // ── 200 OK ────────────────────────────────────────────────────────────

    @Test
    fun `requestToken 200 returns EmployeeTokenResponse with token`() = runTest {
        val engine = MockEngine { _ ->
            respond(
                content = ByteReadChannel("""{"token":"jwt-abc-123"}"""),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            )
        }
        val dataSource = AuthRemoteDataSource(buildClient(engine))

        val response = dataSource.requestToken(code = "EMP001", password = "secret")

        assertEquals("jwt-abc-123", response.token)
    }

    // ── 401 Unauthorized ──────────────────────────────────────────────────

    @Test
    fun `requestToken 401 throws ClientRequestException`() = runTest {
        val engine = MockEngine { _ ->
            respond(
                content = ByteReadChannel("Unauthorized"),
                status = HttpStatusCode.Unauthorized,
                headers = headersOf(HttpHeaders.ContentType, "text/plain")
            )
        }
        val dataSource = AuthRemoteDataSource(buildClient(engine))

        try {
            dataSource.requestToken(code = "EMP001", password = "wrong")
            fail("Expected ClientRequestException to be thrown")
        } catch (e: ClientRequestException) {
            assertEquals(HttpStatusCode.Unauthorized, e.response.status)
        }
    }

    // ── 403 Forbidden ─────────────────────────────────────────────────────

    @Test
    fun `requestToken 403 throws ClientRequestException`() = runTest {
        val engine = MockEngine { _ ->
            respond(
                content = ByteReadChannel("Forbidden"),
                status = HttpStatusCode.Forbidden,
                headers = headersOf(HttpHeaders.ContentType, "text/plain")
            )
        }
        val dataSource = AuthRemoteDataSource(buildClient(engine))

        try {
            dataSource.requestToken(code = "EMP001", password = "pass")
            fail("Expected ClientRequestException to be thrown")
        } catch (e: ClientRequestException) {
            assertEquals(HttpStatusCode.Forbidden, e.response.status)
        }
    }

    // ── 500 Internal Server Error ─────────────────────────────────────────

    @Test
    fun `requestToken 500 throws ServerResponseException`() = runTest {
        val engine = MockEngine { _ ->
            respond(
                content = ByteReadChannel("Internal Server Error"),
                status = HttpStatusCode.InternalServerError,
                headers = headersOf(HttpHeaders.ContentType, "text/plain")
            )
        }
        val dataSource = AuthRemoteDataSource(buildClient(engine))

        try {
            dataSource.requestToken(code = "EMP001", password = "pass")
            fail("Expected ServerResponseException to be thrown")
        } catch (e: ServerResponseException) {
            assertEquals(HttpStatusCode.InternalServerError, e.response.status)
        }
    }

    // ── Request shape ─────────────────────────────────────────────────────

    @Test
    fun `requestToken sends correct JSON body with code and password`() = runTest {
        var capturedBody = ""
        val engine = MockEngine { request ->
            capturedBody = request.body.toByteArray().decodeToString()
            respond(
                content = ByteReadChannel("""{"token":"tok"}"""),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            )
        }
        val dataSource = AuthRemoteDataSource(buildClient(engine))

        dataSource.requestToken(code = "EMP999", password = "myPass")

        assert(capturedBody.contains("EMP999")) {
            "Expected request body to contain 'EMP999', got: $capturedBody"
        }
        assert(capturedBody.contains("myPass")) {
            "Expected request body to contain 'myPass', got: $capturedBody"
        }
    }
}
