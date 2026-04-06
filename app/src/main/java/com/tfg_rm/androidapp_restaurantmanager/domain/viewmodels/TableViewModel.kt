package com.tfg_rm.androidapp_restaurantmanager.domain.viewmodels

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfg_rm.androidapp_restaurantmanager.R
import com.tfg_rm.androidapp_restaurantmanager.domain.models.TableInfoUi
import com.tfg_rm.androidapp_restaurantmanager.domain.models.Tables
import com.tfg_rm.androidapp_restaurantmanager.domain.models.UiState
import com.tfg_rm.androidapp_restaurantmanager.domain.services.TableService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel responsible for managing the restaurant's table layout and occupancy statistics.
 * * This class interacts with the [TableService] to provide the UI with a real-time view of
 * table availability, section filtering, and aggregated status reports (Available vs. Occupied).
 *
 * @property tableService The domain service providing access to table-related operations.
 */
@HiltViewModel
class TableViewModel @Inject constructor(
    private val tableService: TableService
) : ViewModel() {

    /**
     * Currently selected table identifier.
     * Used to track which table the user is interacting with in the UI.
     */
    private val _actualTable = mutableStateOf(1)
    val actualTable: State<Int> = _actualTable

    private val _tables = MutableStateFlow<UiState<List<Tables>>>(UiState.Idle)

    /**
     * Observable state flow representing the current state of the table list.
     * Consumers can observe this to handle [UiState.Loading], [UiState.Success], or [UiState.Error].
     */
    val tables: StateFlow<UiState<List<Tables>>> = _tables.asStateFlow()

    /**
     * Resets the tables UI state to [UiState.Idle].
     */
    fun resetState() {
        _tables.value = UiState.Idle
    }

    /**
     * Updates the currently selected table ID.
     * @param idTable The unique identifier of the selected table.
     */
    fun setTable(idTable: Int) {
        _actualTable.value = idTable
    }

    /**
     * Extracts unique section names (e.g., "Terrace", "Main Hall") from a list of tables.
     * Used for building navigation tabs or filters in the UI.
     */
    fun getSections(tables: List<Tables>): List<String> {
        return tables.map { it.section }.distinct()
    }

    /**
     * Asynchronously fetches the list of tables from the service.
     * Updates [_tables] with the resulting data or a specific error resource on failure.
     */
    fun getTables() {
        viewModelScope.launch {
            try {
                _tables.value = UiState.Loading
                val tablesData = tableService.getTables()
                _tables.value = UiState.Success(tablesData)
            } catch (e: Exception) {
                Log.e("TableViewModel", e.message ?: "Error loading tables")
                _tables.value = UiState.Error(R.string.tablescreen_error)
            }
        }
    }

    /**
     * Generates a list of [TableInfoUi] objects containing occupancy statistics for a specific section.
     * * It calculates the count of Available, Occupied (any status other than AVAILABLE),
     * and Total tables, assigning standardized colors for the UI representation.
     *
     * @param section The name of the section to analyze.
     * @param tablas The full list of tables to filter from.
     * @return A list of UI models representing the summary statistics.
     */
    fun getTableInfo(section: String, tablas: List<Tables>): List<TableInfoUi> {
        val disponibles = tablas.count { it.status.equals("AVAILABLE") && it.section == section }
        val ocupadas = tablas.count { !(it.status.equals("AVAILABLE")) && it.section == section }
        val total = tablas.count { it.section == section }

        return listOf(
            TableInfoUi(
                title = R.string.available,
                count = disponibles,
                color = Color(0xFF4CAF50) // Green
            ),
            TableInfoUi(
                title = R.string.occupied,
                count = ocupadas,
                color = Color(0xFFF44336) // Red
            ),
            TableInfoUi(
                title = R.string.total,
                count = total,
                color = Color(0xFF2196F3) // Blue
            )
        )
    }
}