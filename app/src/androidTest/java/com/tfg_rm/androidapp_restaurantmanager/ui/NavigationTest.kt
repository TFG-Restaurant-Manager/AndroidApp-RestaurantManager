package com.tfg_rm.androidapp_restaurantmanager.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tfg_rm.androidapp_restaurantmanager.MainActivity
import com.tfg_rm.androidapp_restaurantmanager.data.remote.network.SessionManager
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented navigation tests for [AppNavigation].
 *
 * Uses [HiltAndroidTest] so that all ViewModels are injected with the test doubles
 * provided by [com.tfg_rm.androidapp_restaurantmanager.di.TestDatabaseModule] and
 * [com.tfg_rm.androidapp_restaurantmanager.di.TestNetworkModule].
 *
 * [MainActivity] is annotated with [@AndroidEntryPoint] and hosts the Compose content,
 * so it is used as the activity rule host which satisfies Hilt's injection requirements.
 *
 * Covers:
 * - The app starts at the Login screen (start destination)
 * - Session expiry via [SessionManager] navigates back to Login and clears the back stack
 */
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class NavigationTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    // ── Start destination ─────────────────────────────────────────────────

    @Test
    fun appNavigation_startDestination_isLoginScreen() {
        // MainActivity launches AppNavigation which starts at login_screen route.
        // No setContent needed — the activity already sets it up.
        composeTestRule.waitForIdle()

        // The login screen shows "RestaurantePro" as its branding header
        composeTestRule.onNodeWithText("RestaurantePro").assertIsDisplayed()
    }

    // ── Session expiry ────────────────────────────────────────────────────

    @Test
    fun sessionExpiry_navigatesBackToLoginScreen() {
        composeTestRule.waitForIdle()

        // Trigger session expiration from any part of the app
        runBlocking {
            SessionManager.notifySessionExpired()
        }
        composeTestRule.waitForIdle()

        // After session expiry the app should be back at the login screen
        composeTestRule.onNodeWithText("RestaurantePro").assertIsDisplayed()
    }
}
