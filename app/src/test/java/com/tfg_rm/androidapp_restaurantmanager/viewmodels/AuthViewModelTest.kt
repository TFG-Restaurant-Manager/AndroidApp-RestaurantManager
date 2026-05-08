package com.tfg_rm.androidapp_restaurantmanager.viewmodels

import com.tfg_rm.androidapp_restaurantmanager.R
import com.tfg_rm.androidapp_restaurantmanager.data.remote.network.SessionManager
import com.tfg_rm.androidapp_restaurantmanager.domain.services.AuthService
import com.tfg_rm.androidapp_restaurantmanager.domain.viewmodels.AuthState
import com.tfg_rm.androidapp_restaurantmanager.domain.viewmodels.AuthViewModel
import com.tfg_rm.androidapp_restaurantmanager.utils.MainCoroutineRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Unit tests for [AuthViewModel].
 *
 * Covers:
 * - Auto-login (token found / not found)
 * - Credential login (empty fields, valid, invalid credentials, no network, generic error)
 * - resetState
 * - logout
 * - Session expiration via [SessionManager]
 */
@OptIn(ExperimentalCoroutinesApi::class)
class AuthViewModelTest {

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    private lateinit var service: AuthService
    private lateinit var viewModel: AuthViewModel

    @Before
    fun setUp() {
        service = mockk(relaxed = true)
        viewModel = AuthViewModel(service)
    }

    // ── Auto-login (no credentials) ──────────────────────────────────────

    @Test
    fun `login auto - token exists emits Success`() = runTest {
        coEvery { service.loadToken() } returns true

        viewModel.login()

        assertEquals(AuthState.Success, viewModel.authState.value)
    }

    @Test
    fun `login auto - no token emits Idle`() = runTest {
        coEvery { service.loadToken() } returns false

        viewModel.login()

        assertEquals(AuthState.Idle, viewModel.authState.value)
    }

    // ── Login with credentials ────────────────────────────────────────────

    @Test
    fun `login with empty code emits empty-labels Error`() = runTest {
        viewModel.login("", "password")

        assertEquals(
            AuthState.Error(R.string.loginscreen_loginerror_emptylabels),
            viewModel.authState.value
        )
    }

    @Test
    fun `login with empty password emits empty-labels Error`() = runTest {
        viewModel.login("code123", "")

        assertEquals(
            AuthState.Error(R.string.loginscreen_loginerror_emptylabels),
            viewModel.authState.value
        )
    }

    @Test
    fun `login with valid credentials emits Success`() = runTest {
        coEvery { service.requestToken(any(), any()) } returns Unit

        viewModel.login("code123", "pass456")

        assertEquals(AuthState.Success, viewModel.authState.value)
    }

    @Test
    fun `login with invalid credentials maps to invalidCredentials string`() = runTest {
        coEvery { service.requestToken(any(), any()) } throws
                Exception("JSON input: Invalid credentials")

        viewModel.login("code123", "wrongpass")

        assertEquals(
            AuthState.Error(R.string.loginscreen_loginerror_invalidcredentials),
            viewModel.authState.value
        )
    }

    @Test
    fun `login with no network maps to connection string`() = runTest {
        coEvery { service.requestToken(any(), any()) } throws
                Exception("Unable to resolve host example.com")

        viewModel.login("code123", "pass456")

        assertEquals(
            AuthState.Error(R.string.loginscreen_loginerror_conexion),
            viewModel.authState.value
        )
    }

    @Test
    fun `login with generic exception maps to common error string`() = runTest {
        coEvery { service.requestToken(any(), any()) } throws
                Exception("Something unexpected happened")

        viewModel.login("code123", "pass456")

        assertEquals(
            AuthState.Error(R.string.loginscreen_loginerror_common),
            viewModel.authState.value
        )
    }

    // ── resetState ────────────────────────────────────────────────────────

    @Test
    fun `resetState returns to Idle`() = runTest {
        coEvery { service.requestToken(any(), any()) } throws Exception("fail")
        viewModel.login("code", "pass") // drive to Error state

        viewModel.resetState()

        assertEquals(AuthState.Idle, viewModel.authState.value)
    }

    // ── logout ────────────────────────────────────────────────────────────

    @Test
    fun `logout emits LogOut and calls service logout`() = runTest {
        viewModel.logout()

        assertEquals(AuthState.LogOut, viewModel.authState.value)
        coVerify { service.logout() }
    }

    // ── Session expiration ────────────────────────────────────────────────

    @Test
    fun `sessionExpired event transitions state to LogOut`() = runTest {
        SessionManager.notifySessionExpired()

        assertEquals(AuthState.LogOut, viewModel.authState.value)
    }
}
