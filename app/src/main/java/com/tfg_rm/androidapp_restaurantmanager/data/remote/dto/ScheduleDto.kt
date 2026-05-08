package com.tfg_rm.androidapp_restaurantmanager.data.remote.dto

import kotlinx.serialization.Serializable

/**
 * Data Transfer Object (DTO) representing a work shift or schedule.
 *
 * Used to define the time periods assigned to an employee, specifying
 * the exact start and end moments of their workday.
 *
 * @property id Unique identifier of the schedule record.
 * @property startDatetime Start date and time of the shift (usually in ISO 8601 format).
 * @property endDatetime End date and time of the shift (usually in ISO 8601 format).
 */
@Serializable
data class ScheduleDto(
    val id: Int,
    val startDatetime: String,
    val endDatetime: String
)