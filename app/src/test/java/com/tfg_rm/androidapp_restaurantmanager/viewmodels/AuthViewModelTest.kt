package com.tfg_rm.androidapp_restaurantmanager.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tfg_rm.androidapp_restaurantmanager.R
import com.tfg_rm.androidapp_restaurantmanager.domain.services.AuthService
import com.tfg_rm.androidapp_restaurantmanager.domain.viewmodels.AuthState
import com.tfg_rm.androidapp_restaurantmanager.domain.viewmodels.AuthViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AuthViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = UnconfinedTestDispatcher()
    private val mockService = mockk<AuthService>(relaxed = true)
    private lateinit var viewModel: AuthViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = AuthViewModel(mockService)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `login con codigo vacio emite Error`() {
        viewModel.login("", "pass123")

        val state = viewModel.authState.value
        assertTrue(state is AuthState.Error)
        assertEquals(R.string.loginscreen_loginerror_emptylabels, (state as AuthState.Error).msg)
    }

    @Test
    fun `login con password vacio emite Error`() {
        viewModel.login("code123", "")

        val state = viewModel.authState.value
        assertTrue(state is AuthState.Error)
        assertEquals(R.string.loginscreen_loginerror_emptylabels, (state as AuthState.Error).msg)
    }

    @Test
    fun `login exitoso emite Success`() = runTest {
        // relaxed mock ya devuelve Unit para requestToken y connectBS
        viewModel.login("code123", "pass123")

        assertTrue(viewModel.authState.value is AuthState.Success)
    }

    @Test
    fun `login con credenciales invalidas emite Error de credenciales`() = runTest {
        coEvery { mockService.requestToken(any(), any()) } throws
                RuntimeException("JSON input: Invalid credentials")

        viewModel.login("code123", "wrongpass")

        val state = viewModel.authState.value
        assertTrue(state is AuthState.Error)
        assertEquals(
            R.string.loginscreen_loginerror_invalidcredentials,
            (state as AuthState.Error).msg
        )
    }

    @Test
    fun `login sin conexion emite Error de conexion`() = runTest {
        coEvery { mockService.requestToken(any(), any()) } throws
                RuntimeException("Unable to resolve host")

        viewModel.login("code123", "pass123")

        val state = viewModel.authState.value
        assertTrue(state is AuthState.Error)
        assertEquals(
            R.string.loginscreen_loginerror_conexion,
            (state as AuthState.Error).msg
        )
    }

    @Test
    fun `login sin argumentos con token guardado emite Success`() = runTest {
        coEvery { mockService.loadToken() } returns true

        viewModel.login()

        assertTrue(viewModel.authState.value is AuthState.Success)
    }

    @Test
    fun `login sin argumentos sin token guardado emite Idle`() = runTest {
        coEvery { mockService.loadToken() } returns false

        viewModel.login()

        assertTrue(viewModel.authState.value is AuthState.Idle)
    }

    @Test
    fun `resetState restaura estado a Idle`() = runTest {
        viewModel.login("code123", "pass123")
        assertTrue(viewModel.authState.value is AuthState.Success)

        viewModel.resetState()

        assertTrue(viewModel.authState.value is AuthState.Idle)
    }
}
