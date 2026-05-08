package com.tfg_rm.androidapp_restaurantmanager.data.remote.dto

import kotlinx.serialization.Serializable

/**
 * Data Transfer Object (DTO) representing an employee's information.
 *
 * This class is used to receive staff data from the API, integrating
 * their basic information, their role within the restaurant, and their work schedule.
 *
 * @property id Unique identifier of the employee in the system.
 * @property roleName Name of the role or position held (e.g., "Manager", "Waiter").
 * @property name Full name of the employee.
 * @property email Contact email address.
 * @property phone Contact phone number.
 * @property schedules List of [ScheduleDto] objects detailing the assigned shifts or schedules.
 */
@Serializable
data class EmployeesDto(
    val id: Int,
    val roleName: String,
    val name: String,
    val email: String,
    val phone: String,
    val schedules: List<ScheduleDto>
)