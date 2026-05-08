package com.tfg_rm.androidapp_restaurantmanager.network

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.tfg_rm.androidapp_restaurantmanager.data.remote.network.TokenProvider
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented tests for [TokenProvider].
 *
 * Uses the real application-context DataStore ("auth_prefs") created by the
 * [preferencesDataStore] extension property. Each test clears the token in
 * [setUp] to ensure isolation between runs.
 *
 * Covers:
 * - loadToken() returns false when nothing is stored
 * - setToken() persists to DataStore and updates the in-memory cache
 * - getToken() returns the cached value without a disk read
 * - loadToken() returns true after a token has been persisted
 * - clearToken() removes the token from DataStore and nullifies the cache
 */
@RunWith(AndroidJUnit4::class)
class TokenProviderTest {

    private lateinit var tokenProvider: TokenProvider

    @Before
    fun setUp() = runBlocking {
        val ctx = InstrumentationRegistry.getInstrumentation().targetContext
        tokenProvider = TokenProvider(ctx)
        tokenProvider.clearToken() // start each test with a clean slate
    }

    @After
    fun tearDown() = runBlocking {
        tokenProvider.clearToken()
    }

    // ── loadToken ─────────────────────────────────────────────────────────

    @Test
    fun loadToken_returnsFalse_whenNothingStored() = runBlocking {
        val result = tokenProvider.loadToken()
        assertFalse(result)
    }

    @Test
    fun loadToken_returnsTrue_afterTokenIsSet() = runBlocking {
        tokenProvider.setToken("my-jwt-token")

        // Simulate a fresh provider that hasn't loaded yet
        val ctx = InstrumentationRegistry.getInstrumentation().targetContext
        val freshProvider = TokenProvider(ctx)

        val result = freshProvider.loadToken()

        assertTrue(result)
    }

    // ── setToken ──────────────────────────────────────────────────────────

    @Test
    fun setToken_updatesInMemoryCache() = runBlocking {
        tokenProvider.setToken("token-abc")

        assertEquals("token-abc", tokenProvider.getToken())
    }

    @Test
    fun setToken_persistsToDataStore() = runBlocking {
        tokenProvider.setToken("persisted-token")

        // Create a new instance that reads from the same DataStore
        val ctx = InstrumentationRegistry.getInstrumentation().targetContext
        val anotherProvider = TokenProvider(ctx)
        anotherProvider.loadToken()

        assertEquals("persisted-token", anotherProvider.getToken())
    }

    @Test
    fun setToken_overwritesPreviousToken() = runBlocking {
        tokenProvider.setToken("first-token")
        tokenProvider.setToken("second-token")

        assertEquals("second-token", tokenProvider.getToken())
    }

    // ── getToken ──────────────────────────────────────────────────────────

    @Test
    fun getToken_returnsNull_whenNothingLoaded() {
        // No setToken / loadToken called yet in this test
        val ctx = InstrumentationRegistry.getInstrumentation().targetContext
        val freshProvider = TokenProvider(ctx)

        assertNull(freshProvider.getToken())
    }

    @Test
    fun getToken_returnsCurrentCachedValue() = runBlocking {
        tokenProvider.setToken("cached-value")

        assertEquals("cached-value", tokenProvider.getToken())
    }

    // ── clearToken ────────────────────────────────────────────────────────

    @Test
    fun clearToken_nullifiesInMemoryCache() = runBlocking {
        tokenProvider.setToken("some-token")

        tokenProvider.clearToken()

        assertNull(tokenProvider.getToken())
    }

    @Test
    fun clearToken_removesTokenFromDataStore() = runBlocking {
        tokenProvider.setToken("to-be-cleared")
        tokenProvider.clearToken()

        // New instance reads from DataStore — should find nothing
        val ctx = InstrumentationRegistry.getInstrumentation().targetContext
        val freshProvider = TokenProvider(ctx)
        val found = freshProvider.loadToken()

        assertFalse(found)
    }

    @Test
    fun clearToken_whenAlreadyEmpty_doesNotThrow() = runBlocking {
        // Should complete without exception
        tokenProvider.clearToken()
        tokenProvider.clearToken()

        assertFalse(tokenProvider.loadToken())
    }
}
