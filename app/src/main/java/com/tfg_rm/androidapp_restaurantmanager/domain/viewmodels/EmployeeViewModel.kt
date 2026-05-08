package com.tfg_rm.androidapp_restaurantmanager.domain.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfg_rm.androidapp_restaurantmanager.R
import com.tfg_rm.androidapp_restaurantmanager.domain.models.Employee
import com.tfg_rm.androidapp_restaurantmanager.domain.models.UiState
import com.tfg_rm.androidapp_restaurantmanager.domain.services.EmployeeService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel responsible for managing the state and retrieval of employee profile data.
 *
 * This class interacts with the [EmployeeService] to fetch personal information and schedules
 * for the authenticated user. it exposes a reactive [UiState] that allows the UI to
 * display loading indicators, success data, or localized error messages.
 *
 * @property service The domain service that provides access to employee-related logic.
 */
@HiltViewModel
class EmployeeViewModel @Inject constructor(
    private val service: EmployeeService
) : ViewModel() {

    private val _employeeState = MutableStateFlow<UiState<Employee>>(UiState.Idle)

    /**
     * Observable state flow representing the current lifecycle of the employee data request.
     * Use this in the UI to observe changes between [UiState.Idle], [UiState.Loading],
     * [UiState.Success], and [UiState.Error].
     */
    val employeeState: StateFlow<UiState<Employee>> = _employeeState.asStateFlow()

    /**
     * Manually resets the employee state to [UiState.Idle].
     * Typically used when navigating away from the profile or clearing previous error states.
     */
    fun resetState() {
        _employeeState.value = UiState.Idle
    }

    /**
     * Initiates an asynchronous request to fetch the current employee's data.
     * * Updates the [_employeeState] to [UiState.Loading] during the process and transitions
     * to [UiState.Success] upon receiving data. If the request fails, it catches the
     * exception and sets the state to [UiState.Error] with a specific string resource.
     */
    fun getEmployeeData() {
        viewModelScope.launch {
            _employeeState.value = UiState.Loading
            try {
                val data = service.getEmployeeData()
                _employeeState.value = UiState.Success(data)
                Log.i("EmployeeViewModel", "Employee data received successfully")
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("EmployeeViewModel", e.message ?: "Employee data error")
                _employeeState.value = UiState.Error(R.string.profilescreen_loadingerror)
            }
        }
    }
}