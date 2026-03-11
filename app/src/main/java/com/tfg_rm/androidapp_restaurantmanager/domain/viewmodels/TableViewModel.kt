package com.tfg_rm.androidapp_restaurantmanager.domain.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class TableViewModel: ViewModel() {

    private val _actualTable = mutableStateOf(1)

    val actualTable: State<Int> = _actualTable

    fun setTable (idTable: Int) {
        _actualTable.value = idTable
    }
}