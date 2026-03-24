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

@HiltViewModel
class TableViewModel @Inject constructor(
    private val tableService: TableService
) : ViewModel() {

    private val _actualTable = mutableStateOf(1)

    val actualTable: State<Int> = _actualTable

    private val _tables = MutableStateFlow<UiState<List<Tables>>>(UiState.Idle)
    val tables: StateFlow<UiState<List<Tables>>> = _tables.asStateFlow()

    fun resetState() {
        _tables.value = UiState.Idle
    }

    fun setTable(idTable: Int) {
        _actualTable.value = idTable
    }

    fun getSections(tables: List<Tables>): List<String> {
        return tables.map { it.section }.distinct()
    }

    fun getTables() {
        viewModelScope.launch {
            try {
                _tables.value = UiState.Loading
                val tablesData = tableService.getTables()
                _tables.value = UiState.Success(tablesData)
            } catch (e: Exception) {
                Log.e("TableViewModel", e.message ?: "Error al cargar las mesas")
                _tables.value = UiState.Error(R.string.tablescreen_error)
            }
        }
    }

    fun getTableInfo(section: String, tablas: List<Tables>): List<TableInfoUi> {
        val disponibles = tablas.count { it.status.equals("AVAILABLE") && it.section == section }
        val ocupadas = tablas.count { !(it.status.equals("AVAILABLE")) && it.section == section }
        val total = tablas.count { it.section == section }

        return listOf(
            TableInfoUi(
                title = R.string.available,
                count = disponibles,
                color = Color(0xFF4CAF50) // verde
            ),
            TableInfoUi(
                title = R.string.occupied,
                count = ocupadas,
                color = Color(0xFFF44336) // rojo
            ),
            TableInfoUi(
                title = R.string.total,
                count = total,
                color = Color(0xFF2196F3) // azul
            )
        )
    }
}