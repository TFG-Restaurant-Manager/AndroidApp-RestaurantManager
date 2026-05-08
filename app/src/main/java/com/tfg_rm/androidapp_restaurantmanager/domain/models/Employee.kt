package com.tfg_rm.androidapp_restaurantmanager.domain.models

import java.time.LocalDateTime

/**
 * Domain model representing an employee within the restaurant management system.
 *
 * This class encapsulates the personal, professional, and logistical information of a staff member,
 * including their contact details and their historical or upcoming work shifts.
 *
 * @property roleName The job title or position held by the employee (e.g., "Waiter", "Chef").
 * @property name The full name of the employee.
 * @property email The contact email address for the employee.
 * @property phone The contact telephone number for the employee.
 * @property schedules A list of work shifts, where each [Pair] represents the start and end [LocalDateTime] of a session.
 */
data class Employee(
    val roleName: String,
    val name: String,
    val email: String,
    val phone: String,
    val schedules: List<Pair<LocalDateTime, LocalDateTime>>
)