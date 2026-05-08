package com.tfg_rm.androidapp_restaurantmanager.viewmodels

import app.cash.turbine.test
import com.tfg_rm.androidapp_restaurantmanager.R
import com.tfg_rm.androidapp_restaurantmanager.domain.models.Employee
import com.tfg_rm.androidapp_restaurantmanager.domain.models.UiState
import com.tfg_rm.androidapp_restaurantmanager.domain.services.EmployeeService
import com.tfg_rm.androidapp_restaurantmanager.domain.viewmodels.EmployeeViewModel
import com.tfg_rm.androidapp_restaurantmanager.utils.MainCoroutineRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Unit tests for [EmployeeViewModel].
 *
 * Covers:
 * - getEmployeeData() → Loading → Success / Error transitions
 * - resetState() → Idle
 */
@OptIn(ExperimentalCoroutinesApi::class)
class EmployeeViewModelTest {

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    private lateinit var service: EmployeeService
    private lateinit var viewModel: EmployeeViewModel

    private val sampleEmployee = Employee(
        roleName = "Waiter",
        name = "John Doe",
        email = "john@restaurant.com",
        phone = "+34 600 000 000",
        schedules = emptyList()
    )

    @Before
    fun setUp() {
        service = mockk(relaxed = true)
        viewModel = EmployeeViewModel(service)
    }

    // ── getEmployeeData ───────────────────────────────────────────────────

    @Test
    fun `getEmployeeData emits Loading then Success`() = runTest {
        coEvery { service.getEmployeeData() } returns sampleEmployee

        viewModel.employeeState.test {
            awaitItem() // initial Idle

            viewModel.getEmployeeData()

            val loading = awaitItem()
            assertEquals(UiState.Loading, loading)

            val success = awaitItem()
            assertTrue(success is UiState.Success)
            assertEquals(sampleEmployee, (success as UiState.Success).data)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getEmployeeData emits Error on exception`() = runTest {
        coEvery { service.getEmployeeData() } throws Exception("Server error")

        viewModel.employeeState.test {
            awaitItem() // initial Idle

            viewModel.getEmployeeData()

            awaitItem() // Loading
            val error = awaitItem()

            assertTrue(error is UiState.Error)
            assertEquals(R.string.profilescreen_loadingerror, (error as UiState.Error).message)

            cancelAndIgnoreRemainingEvents()
        }
    }

    // ── resetState ────────────────────────────────────────────────────────

    @Test
    fun `resetState returns to Idle after Success`() = runTest {
        coEvery { service.getEmployeeData() } returns sampleEmployee
        viewModel.getEmployeeData()
        assertTrue(viewModel.employeeState.value is UiState.Success)

        viewModel.resetState()

        assertEquals(UiState.Idle, viewModel.employeeState.value)
    }

    @Test
    fun `resetState returns to Idle after Error`() = runTest {
        coEvery { service.getEmployeeData() } throws Exception("fail")
        viewModel.getEmployeeData()
        assertTrue(viewModel.employeeState.value is UiState.Error)

        viewModel.resetState()

        assertEquals(UiState.Idle, viewModel.employeeState.value)
    }
}
