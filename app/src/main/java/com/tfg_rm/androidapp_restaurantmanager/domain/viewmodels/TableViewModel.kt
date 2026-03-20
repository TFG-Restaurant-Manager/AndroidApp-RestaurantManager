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
import kotlinx.coroutines.delay
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

    fun setTable(idTable: Int) {
        _actualTable.value = idTable
    }

    fun getSections(): List<String> {
        return listOf("Section 1", "Section 2")
    }

    fun getTables() {
        viewModelScope.launch {
            try {
                _tables.value = UiState.Loading
                delay(1200)
                _tables.value = UiState.Success<List<Tables>>(
                    listOf(
                        Tables(
                            id = 0,
                            capacity = 4,
                            section = 1,
                            posX = 0,
                            posY = 0,
                            available = true
                        ),

                        Tables(
                            id = 1,
                            capacity = 4,
                            section = 1,
                            posX = 1,
                            posY = 1,
                            available = true
                        ),
                        Tables(
                            id = 3,
                            capacity = 6,
                            section = 1,
                            posX = 1,
                            posY = 2,
                            available = true
                        ),
                        Tables(
                            id = 2,
                            capacity = 2,
                            section = 1,
                            posX = 2,
                            posY = 2,
                            available = false
                        ),
                        Tables(
                            id = 4,
                            capacity = 4,
                            section = 1,
                            posX = 2,
                            posY = 1,
                            available = true
                        ),

                        //    Tables(id = 5, capacity = 4, section = 1, posX = 3, posY = 1, available = true),
                        //    Tables(id = 6, capacity = 6, section = 1, posX = 3, posY = 2, available = true),
                        Tables(
                            id = 7,
                            capacity = 2,
                            section = 1,
                            posX = 3,
                            posY = 3,
                            available = false
                        ),
                        Tables(
                            id = 8,
                            capacity = 4,
                            section = 1,
                            posX = 4,
                            posY = 4,
                            available = true
                        ),

                        //    Tables(id = 9, capacity = 4, section = 1, posX = 5, posY = 5, available = true),
                        //    Tables(id = 10, capacity = 6, section = 1, posX = 6, posY = 6, available = true),
                        //    Tables(id = 11, capacity = 2, section = 1, posX = 7, posY = 7, available = false),
                        //    Tables(id = 12, capacity = 4, section = 1, posX = 8, posY = 8, available = true),
                        //
                        //    Tables(id = 13, capacity = 4, section = 1, posX = 9, posY = 9, available = true),
                        //    Tables(id = 14, capacity = 6, section = 1, posX = 10, posY = 10, available = true),
                        //    Tables(id = 15, capacity = 2, section = 1, posX = 11, posY = 11, available = false),
                        //    Tables(id = 16, capacity = 4, section = 1, posX = 12, posY = 12, available = true),
                        //
                        //    Tables(id = 17, capacity = 4, section = 1, posX = 13, posY = 13, available = true),
                        //    Tables(id = 18, capacity = 6, section = 1, posX = 14, posY = 14, available = true),
                        //    Tables(id = 19, capacity = 2, section = 1, posX = 15, posY = 15, available = false),
                        //    Tables(id = 20, capacity = 4, section = 1, posX = 16, posY = 16, available = true),
                        //    Tables(id = 21, capacity = 4, section = 1, posX = 16, posY = 17, available = true),
                        //    Tables(id = 22, capacity = 4, section = 1, posX = 17, posY = 16, available = true),
                        //
                        //
                        //    Tables(id = 1, capacity = 3, section = 2, posX = 1, posY = 1, available = true),
                        //    Tables(id = 3, capacity = 7, section = 2, posX = 1, posY = 2, available = true),
                        //    Tables(id = 2, capacity = 1, section = 2, posX = 2, posY = 2, available = false),
                        //    Tables(id = 4, capacity = 3, section = 2, posX = 2, posY = 1, available = true))
                    )
                )
            } catch (e: Exception) {
                Log.e("TableViewModel", e.message ?: "Error al cargar las mesas")
                _tables.value = UiState.Error(R.string.tablescreen_error)
            }
        }
    }

    fun getTableInfo(section: Int): List<TableInfoUi> {
        val tablas = (_tables.value as UiState.Success).data
        val disponibles = tablas.count { it.available && it.section == section }
        val ocupadas = tablas.count { !it.available && it.section == section }
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