package com.tfg_rm.androidapp_restaurantmanager.domain.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfg_rm.androidapp_restaurantmanager.R
import com.tfg_rm.androidapp_restaurantmanager.data.remote.network.SocketEvent
import com.tfg_rm.androidapp_restaurantmanager.domain.models.Employee
import com.tfg_rm.androidapp_restaurantmanager.domain.models.UiState
import com.tfg_rm.androidapp_restaurantmanager.domain.services.EmployeeService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmployeeViewModel @Inject constructor(
    private val service: EmployeeService
) : ViewModel() {

    init {
        observeSocket()
    }

    private val _employeeState = MutableStateFlow<UiState<Employee>>(UiState.Idle)
    val employeeState: StateFlow<UiState<Employee>> = _employeeState.asStateFlow()

    fun resetState() {
        _employeeState.value = UiState.Idle
    }

    fun getEmployeeData() {
        viewModelScope.launch {
            _employeeState.value = UiState.Loading
            try {
                val data = service.getEmployeeData()
                _employeeState.value = UiState.Success(data)
                Log.i("EmployeeViewModel", "Employee data recieved succesfully")
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("EmployeeViewModel", e.message ?: "Employee data error")
                _employeeState.value = UiState.Error(R.string.profilescreen_loadingerror)
            }
        }
    }

    private fun observeSocket() {
        viewModelScope.launch {
            service.events.collect { event ->
                when (event) {
                    is SocketEvent.EmployeeUpdated -> {
                        Log.i("EmployeeViewModel", "Empleado actualizado: ${event.data}")
                    }

                    else -> {}
                }
            }
        }
    }
}