package com.tfg_rm.androidapp_restaurantmanager.domain.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.tfg_rm.androidapp_restaurantmanager.domain.services.TableService

@HiltViewModel
class TableViewModel @Inject constructor(
    private val tableService: TableService
) : ViewModel() {

    private val _actualTable = mutableStateOf(1)

    val actualTable: State<Int> = _actualTable

    fun setTable (idTable: Int) {
        _actualTable.value = idTable
    }
}