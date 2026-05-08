package com.tfg_rm.androidapp_restaurantmanager.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tfg_rm.androidapp_restaurantmanager.R
import com.tfg_rm.androidapp_restaurantmanager.domain.models.Tables
import com.tfg_rm.androidapp_restaurantmanager.domain.models.UiState
import com.tfg_rm.androidapp_restaurantmanager.domain.services.TableService
import com.tfg_rm.androidapp_restaurantmanager.domain.viewmodels.TableViewModel
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
class TableViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = UnconfinedTestDispatcher()
    private val mockService = mockk<TableService>()
    private lateinit var viewModel: TableViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = TableViewModel(mockService)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // ── Funciones puras (sin coroutines) ──────────────────────────────────────

    @Test
    fun `getSections retorna secciones unicas sin duplicados`() {
        val tables = listOf(
            Tables(1, "T1", 4, "Terraza", 0.0, 0.0, "AVAILABLE"),
            Tables(2, "T2", 2, "Interior", 0.0, 0.0, "OCCUPIED"),
            Tables(3, "T3", 6, "Terraza", 0.0, 0.0, "AVAILABLE"),
            Tables(4, "T4", 4, "Interior", 0.0, 0.0, "AVAILABLE"),
        )

        val sections = viewModel.getSections(tables)

        assertEquals(listOf("Terraza", "Interior"), sections)
    }

    @Test
    fun `getSections con lista vacia retorna lista vacia`() {
        val sections = viewModel.getSections(emptyList())
        assertTrue(sections.isEmpty())
    }

    @Test
    fun `getTableInfo cuenta correctamente disponibles y ocupadas por seccion`() {
        val tables = listOf(
            Tables(1, "T1", 4, "Terraza", 0.0, 0.0, "AVAILABLE"),
            Tables(2, "T2", 2, "Terraza", 0.0, 0.0, "OCCUPIED"),
            Tables(3, "T3", 6, "Terraza", 0.0, 0.0, "AVAILABLE"),
            Tables(4, "T4", 2, "Interior", 0.0, 0.0, "AVAILABLE"), // otra seccion: no cuenta
        )

        val info = viewModel.getTableInfo("Terraza", tables)

        val disponibles = info.first { it.title == R.string.available }
        val ocupadas = info.first { it.title == R.string.occupied }
        val total = info.first { it.title == R.string.total }

        assertEquals(2, disponibles.count)
        assertEquals(1, ocupadas.count)
        assertEquals(3, total.count)
    }

    @Test
    fun `getTableInfo solo cuenta mesas de la seccion solicitada`() {
        val tables = listOf(
            Tables(1, "T1", 4, "Terraza", 0.0, 0.0, "AVAILABLE"),
            Tables(2, "T2", 2, "Interior", 0.0, 0.0, "AVAILABLE"),
        )

        val info = viewModel.getTableInfo("Interior", tables)
        val total = info.first { it.title == R.string.total }

        assertEquals(1, total.count)
    }

    // ── Flujos asincrónos ─────────────────────────────────────────────────────

    @Test
    fun `getTables exitoso actualiza estado a UiState Success`() = runTest {
        val tablesList = listOf(Tables(1, "T1", 4, "Terraza", 0.0, 0.0, "AVAILABLE"))
        coEvery { mockService.getTables() } returns tablesList

        viewModel.getTables()

        val state = viewModel.tables.value
        assertTrue(state is UiState.Success)
        assertEquals(tablesList, (state as UiState.Success).data)
    }

    @Test
    fun `getTables con error actualiza estado a UiState Error`() = runTest {
        coEvery { mockService.getTables() } throws RuntimeException("Network failure")

        viewModel.getTables()

        val state = viewModel.tables.value
        assertTrue(state is UiState.Error)
        assertEquals(R.string.tablescreen_error, (state as UiState.Error).message)
    }

    @Test
    fun `resetState restaura tables a UiState Idle`() = runTest {
        coEvery { mockService.getTables() } returns emptyList()
        viewModel.getTables()
        assertTrue(viewModel.tables.value is UiState.Success)

        viewModel.resetState()

        assertTrue(viewModel.tables.value is UiState.Idle)
    }
}
