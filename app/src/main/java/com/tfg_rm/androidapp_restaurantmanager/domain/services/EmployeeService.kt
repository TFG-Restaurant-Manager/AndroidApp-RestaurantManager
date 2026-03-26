package com.tfg_rm.androidapp_restaurantmanager.domain.services

import com.tfg_rm.androidapp_restaurantmanager.data.repository.EmployeeRepository
import com.tfg_rm.androidapp_restaurantmanager.domain.models.Employee
import javax.inject.Inject

class EmployeeService @Inject constructor(
    private val repository: EmployeeRepository
) {
    suspend fun getEmployeeData(): Employee {
        return repository.getEmployeeData()
    }
}