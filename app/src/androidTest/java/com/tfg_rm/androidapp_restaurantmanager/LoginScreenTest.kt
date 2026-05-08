package com.tfg_rm.androidapp_restaurantmanager

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tfg_rm.androidapp_restaurantmanager.domain.viewmodels.AuthState
import com.tfg_rm.androidapp_restaurantmanager.domain.viewmodels.AuthViewModel
import com.tfg_rm.androidapp_restaurantmanager.ui.screens.LoginScreen
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val fakeAuthState = MutableStateFlow<AuthState>(AuthState.Idle)
    private lateinit var viewModel: AuthViewModel

    @Before
    fun setup() {
        viewModel = mockk(relaxed = true)
        every { viewModel.authState } returns fakeAuthState.asStateFlow()
    }

    private fun setLoginScreen() {
        composeRule.setContent {
            LoginScreen(
                authViewModel = viewModel,
                loginSuccess = {},
                recargarEstados = {}
            )
        }
    }

    @Test
    fun loginScreen_muestraElNombreDeApp() {
        setLoginScreen()
        composeRule.onNodeWithText("RestaurantePro").assertIsDisplayed()
    }

    @Test
    fun loginScreen_muestraEtiquetaDeCodigo() {
        setLoginScreen()
        composeRule.onNodeWithText("Employee User").assertIsDisplayed()
    }

    @Test
    fun loginScreen_muestraEtiquetaDePassword() {
        setLoginScreen()
        composeRule.onNodeWithText("Password").assertIsDisplayed()
    }

    @Test
    fun loginScreen_muestraBotonLogin() {
        setLoginScreen()
        composeRule.onNodeWithText("Login").assertIsDisplayed()
    }

    @Test
    fun loginScreen_alHacerClickEnLoginLlamaAlViewModel() {
        setLoginScreen()
        composeRule.onNodeWithText("Login").performClick()

        verify { viewModel.login(any(), any()) }
    }

    @Test
    fun loginScreen_enEstadoLoadingMuestraTextoLoading() {
        fakeAuthState.value = AuthState.Loading
        setLoginScreen()

        composeRule.onNodeWithText("Loading...").assertIsDisplayed()
    }
}
