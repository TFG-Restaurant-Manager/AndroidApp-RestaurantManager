package com.tfg_rm.androidapp_restaurantmanager.domain.services

import com.tfg_rm.androidapp_restaurantmanager.data.repository.EmployeeRepository
import com.tfg_rm.androidapp_restaurantmanager.domain.models.Employee
import javax.inject.Inject

/**
 * Domain service that manages employee-related business logic.
 *
 * This service acts as a clean entry point for the presentation layer to access
 * employee information, abstracting the underlying data retrieval logic provided
 * by the [EmployeeRepository].
 *
 * @property repository The repository used to fetch and map employee data from the data source.
 */
class EmployeeService @Inject constructor(
    private val repository: EmployeeRepository
) {

    /**
     * Retrieves the profile and schedule information of the current employee.
     *
     * @return An [Employee] domain model containing the relevant staff data.
     * @throws Exception If there is a failure during the data retrieval process.
     */
    suspend fun getEmployeeData(): Employee {
        return repository.getEmployeeData()
    }
}