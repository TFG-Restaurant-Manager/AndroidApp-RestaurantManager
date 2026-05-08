package com.tfg_rm.androidapp_restaurantmanager.viewmodels

import app.cash.turbine.test
import com.tfg_rm.androidapp_restaurantmanager.R
import com.tfg_rm.androidapp_restaurantmanager.domain.models.Tables
import com.tfg_rm.androidapp_restaurantmanager.domain.models.UiState
import com.tfg_rm.androidapp_restaurantmanager.domain.services.TableService
import com.tfg_rm.androidapp_restaurantmanager.domain.viewmodels.TableViewModel
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
 * Unit tests for [TableViewModel].
 *
 * Covers:
 * - getTables() → Loading → Success / Error transitions
 * - resetState() → Idle
 * - getSections() returns distinct, ordered section names
 * - setTable() updates the selected table
 */
@OptIn(ExperimentalCoroutinesApi::class)
class TableViewModelTest {

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    private lateinit var service: TableService
    private lateinit var viewModel: TableViewModel

    private fun table(id: Int, section: String, status: String = "AVAILABLE") = Tables(
        id = id, name = "T$id", capacity = 4, section = section,
        posX = 0.0, posY = 0.0, status = status
    )

    @Before
    fun setUp() {
        service = mockk(relaxed = true)
        viewModel = TableViewModel(service)
    }

    // ── getTables ─────────────────────────────────────────────────────────

    @Test
    fun `getTables emits Loading then Success with data`() = runTest {
        val tableList = listOf(table(1, "Main"), table(2, "Terrace"))
        coEvery { service.getTables() } returns tableList

        viewModel.tables.test {
            awaitItem() // initial Idle

            viewModel.getTables()

            val loading = awaitItem()
            assertEquals(UiState.Loading, loading)

            val success = awaitItem()
            assertTrue(success is UiState.Success)
            assertEquals(2, (success as UiState.Success).data.size)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getTables emits Error on exception`() = runTest {
        coEvery { service.getTables() } throws Exception("Server unavailable")

        viewModel.tables.test {
            awaitItem() // initial Idle

            viewModel.getTables()

            awaitItem() // Loading
            val error = awaitItem()

            assertTrue(error is UiState.Error)
            assertEquals(R.string.tablescreen_error, (error as UiState.Error).message)

            cancelAndIgnoreRemainingEvents()
        }
    }

    // ── resetState ────────────────────────────────────────────────────────

    @Test
    fun `resetState returns to Idle`() = runTest {
        coEvery { service.getTables() } returns emptyList()
        viewModel.getTables()

        viewModel.resetState()

        assertEquals(UiState.Idle, viewModel.tables.value)
    }

    // ── getSections ───────────────────────────────────────────────────────

    @Test
    fun `getSections returns distinct list`() {
        val tables = listOf(
            table(1, "Main"), table(2, "Main"),
            table(3, "Terrace"), table(4, "Bar")
        )

        val sections = viewModel.getSections(tables)

        assertEquals(listOf("Main", "Terrace", "Bar"), sections)
    }

    @Test
    fun `getSections returns empty list for empty input`() {
        assertEquals(emptyList<String>(), viewModel.getSections(emptyList()))
    }

    // ── setTable ──────────────────────────────────────────────────────────

    @Test
    fun `setTable updates actualTable`() {
        val newTable = table(42, "VIP", "OCCUPIED")

        viewModel.setTable(newTable)

        assertEquals(newTable, viewModel.actualTable.value)
    }
}
