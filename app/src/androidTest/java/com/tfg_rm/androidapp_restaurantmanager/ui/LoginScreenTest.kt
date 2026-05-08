package com.tfg_rm.androidapp_restaurantmanager.ui

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tfg_rm.androidapp_restaurantmanager.R
import com.tfg_rm.androidapp_restaurantmanager.domain.services.AuthService
import com.tfg_rm.androidapp_restaurantmanager.domain.viewmodels.AuthState
import com.tfg_rm.androidapp_restaurantmanager.domain.viewmodels.AuthViewModel
import com.tfg_rm.androidapp_restaurantmanager.ui.screens.LoginScreen
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented Compose UI tests for [LoginScreen].
 *
 * [LoginScreen] accepts [AuthViewModel] as a constructor parameter, so no Hilt injection
 * is needed — the ViewModel is created directly with a mocked [AuthService].
 *
 * Covers:
 * - Branding header and subtitle are displayed
 * - Login button is shown when state is Idle
 * - Loading text is shown when state is Loading
 * - [loginSuccess] callback is invoked when state transitions to Success
 * - Login button click with empty fields keeps state as Error
 */
@RunWith(AndroidJUnit4::class)
class LoginScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    // ── helpers ────────────────────────────────────────────────────────────

    private fun getString(id: Int) = composeTestRule.activity.getString(id)

    private fun loginScreen(
        service: AuthService = mockk(relaxed = true),
        loginSuccess: () -> Unit = {},
        recargarEstados: () -> Unit = {}
    ) {
        val viewModel = AuthViewModel(service)
        composeTestRule.setContent {
            LoginScreen(
                authViewModel = viewModel,
                loginSuccess = loginSuccess,
                recargarEstados = recargarEstados
            )
        }
    }

    // ── branding ──────────────────────────────────────────────────────────

    @Test
    fun loginScreen_showsBrandingTitle() {
        val service = mockk<AuthService>(relaxed = true)
        coEvery { service.loadToken() } returns false

        loginScreen(service = service)
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("RestaurantePro").assertIsDisplayed()
    }

    @Test
    fun loginScreen_showsSubtitle() {
        val service = mockk<AuthService>(relaxed = true)
        coEvery { service.loadToken() } returns false

        loginScreen(service = service)
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText(getString(R.string.loginscreen_subtitle)).assertIsDisplayed()
    }

    // ── Idle state: login button visible ─────────────────────────────────

    @Test
    fun loginScreen_idle_showsLoginButton() {
        val service = mockk<AuthService>(relaxed = true)
        coEvery { service.loadToken() } returns false

        loginScreen(service = service)
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText(getString(R.string.login)).assertIsDisplayed()
    }

    // ── Loading state ─────────────────────────────────────────────────────

    @Test
    fun loginScreen_loading_showsLoadingText() {
        val service = mockk<AuthService>(relaxed = true)
        // loadToken returns false → Idle; then requestToken suspends indefinitely → Loading
        coEvery { service.loadToken() } returns false
        coEvery { service.requestToken(any(), any()) } coAnswers {
            // Simulate work in progress
            kotlinx.coroutines.delay(Long.MAX_VALUE)
        }

        val viewModel = AuthViewModel(service)
        composeTestRule.setContent {
            LoginScreen(
                authViewModel = viewModel,
                loginSuccess = {},
                recargarEstados = {}
            )
        }
        composeTestRule.waitForIdle()

        // Trigger login with valid-looking input to drive to Loading state
        viewModel.login("EMP001", "secret")
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText(getString(R.string.loading_generic)).assertIsDisplayed()
    }

    // ── Success state → loginSuccess callback ─────────────────────────────

    @Test
    fun loginScreen_success_invokesLoginSuccessCallback() {
        val service = mockk<AuthService>(relaxed = true)
        coEvery { service.loadToken() } returns false
        coEvery { service.requestToken(any(), any()) } returns Unit

        var callbackInvoked = false
        val viewModel = AuthViewModel(service)

        composeTestRule.setContent {
            LoginScreen(
                authViewModel = viewModel,
                loginSuccess = { callbackInvoked = true },
                recargarEstados = {}
            )
        }
        composeTestRule.waitForIdle()

        // Drive ViewModel to Success state
        runBlocking { viewModel.login("EMP001", "pass") }
        composeTestRule.waitForIdle()

        assertTrue("loginSuccess callback should have been invoked", callbackInvoked)
    }

    // ── Error state from empty fields ─────────────────────────────────────

    @Test
    fun loginScreen_emptyFields_loginButtonClickSetsErrorState() {
        val service = mockk<AuthService>(relaxed = true)
        coEvery { service.loadToken() } returns false

        val viewModel = AuthViewModel(service)
        composeTestRule.setContent {
            LoginScreen(
                authViewModel = viewModel,
                loginSuccess = {},
                recargarEstados = {}
            )
        }
        composeTestRule.waitForIdle()

        // Click login button without filling fields → empty code + empty password
        composeTestRule.onNodeWithText(getString(R.string.login)).performClick()
        composeTestRule.waitForIdle()

        // Error state sets the ViewModel to AuthState.Error
        assertTrue(viewModel.authState.value is AuthState.Error)
    }
}
