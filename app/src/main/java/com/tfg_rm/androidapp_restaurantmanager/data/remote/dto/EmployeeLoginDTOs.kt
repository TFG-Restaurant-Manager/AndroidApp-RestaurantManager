package com.tfg_rm.androidapp_restaurantmanager.data.remote.dto

import kotlinx.serialization.Serializable

/**
 * Data Transfer Object (DTO) used to send an employee's access credentials.
 *
 * @property dni The employee's National Identity Document, used as a unique identifier for login.
 * @property password Security password associated with the employee's account.
 */
@Serializable
data class EmployeeLoginRequest(
    val dni: String,
    val password: String
)

/**
 * Data Transfer Object (DTO) containing the information returned by the server after a successful authentication.
 *
 * @property employeeId Persistent unique identifier of the employee in the system.
 * @property restaurantIds List of identifiers for the restaurants the employee has access permissions for.
 */
@Serializable
data class EmployeeLoginResponse(
    val employeeId: Long,
    val restaurantIds: List<Long>
)
