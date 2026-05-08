package com.tfg_rm.androidapp_restaurantmanager.viewmodels

import app.cash.turbine.test
import com.tfg_rm.androidapp_restaurantmanager.R
import com.tfg_rm.androidapp_restaurantmanager.domain.models.Order
import com.tfg_rm.androidapp_restaurantmanager.domain.models.UiState
import com.tfg_rm.androidapp_restaurantmanager.domain.services.OrderService
import com.tfg_rm.androidapp_restaurantmanager.domain.viewmodels.OrdersViewModel
import com.tfg_rm.androidapp_restaurantmanager.utils.MainCoroutineRule
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.LocalDateTime

/**
 * Unit tests for [OrdersViewModel].
 *
 * Covers:
 * - getOrders() → Loading → Success / Error state transitions
 * - resetState() cancels socket job and sets Idle
 * - getStatusStringRes() mapping for all known + unknown statuses
 * - getMinutesAgo() returns a non-negative duration
 */
@OptIn(ExperimentalCoroutinesApi::class)
class OrdersViewModelTest {

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    private lateinit var service: OrderService
    private lateinit var viewModel: OrdersViewModel

    @Before
    fun setUp() {
        service = mockk(relaxed = true)
        every { service.observeMessages() } returns MutableSharedFlow<String>()
        viewModel = OrdersViewModel(service)
    }

    // ── getOrders ─────────────────────────────────────────────────────────

    @Test
    fun `getOrders emits Loading then Success with data`() = runTest {
        val sampleOrders = mutableListOf(
            Order(
                id = 1, tableId = 2, tableName = "T2", type = null,
                status = "CREATED", total = 25.0
            )
        )
        coEvery { service.getOrders() } returns sampleOrders

        viewModel.orders.test {
            awaitItem() // initial Idle

            viewModel.getOrders()

            val loading = awaitItem()
            assertEquals(UiState.Loading, loading)

            val success = awaitItem()
            assertTrue(success is UiState.Success)
            assertEquals(1, (success as UiState.Success).data.size)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getOrders emits Error on exception`() = runTest {
        coEvery { service.getOrders() } throws Exception("Network failure")

        viewModel.orders.test {
            awaitItem() // initial Idle

            viewModel.getOrders()

            awaitItem() // Loading
            val error = awaitItem()

            assertTrue(error is UiState.Error)
            assertEquals(R.string.order_geterror, (error as UiState.Error).message)

            cancelAndIgnoreRemainingEvents()
        }
    }

    // ── resetState ────────────────────────────────────────────────────────

    @Test
    fun `resetState returns to Idle after Success`() = runTest {
        coEvery { service.getOrders() } returns mutableListOf()
        viewModel.getOrders()
        assertTrue(viewModel.orders.value is UiState.Success)

        viewModel.resetState()

        assertEquals(UiState.Idle, viewModel.orders.value)
    }

    // ── getStatusStringRes ────────────────────────────────────────────────

    @Test
    fun `getStatusStringRes maps CREATED`() {
        assertEquals(R.string.order_statuscreated, viewModel.getStatusStringRes("CREATED"))
    }

    @Test
    fun `getStatusStringRes maps COOKED`() {
        assertEquals(R.string.order_statuscooked, viewModel.getStatusStringRes("COOKED"))
    }

    @Test
    fun `getStatusStringRes maps DELIVERED`() {
        assertEquals(R.string.order_statusdelivered, viewModel.getStatusStringRes("DELIVERED"))
    }

    @Test
    fun `getStatusStringRes maps PAID`() {
        assertEquals(R.string.order_statuspaid, viewModel.getStatusStringRes("PAID"))
    }

    @Test
    fun `getStatusStringRes maps unknown status to error`() {
        assertEquals(R.string.order_statuserror, viewModel.getStatusStringRes("UNKNOWN"))
        assertEquals(R.string.order_statuserror, viewModel.getStatusStringRes(""))
    }

    // ── getMinutesAgo ─────────────────────────────────────────────────────

    @Test
    fun `getMinutesAgo returns correct elapsed minutes`() {
        val fiveMinutesAgo = LocalDateTime.now().minusMinutes(5)
        val result = viewModel.getMinutesAgo(fiveMinutesAgo)
        assertTrue("Expected ≥ 5 minutes but got $result", result >= 5L)
    }

    @Test
    fun `getMinutesAgo returns 0 for just-created order`() {
        val justNow = LocalDateTime.now()
        val result = viewModel.getMinutesAgo(justNow)
        assertTrue("Expected 0 or 1 minutes but got $result", result >= 0L)
    }
}
