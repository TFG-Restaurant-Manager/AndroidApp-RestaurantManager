package com.tfg_rm.androidapp_restaurantmanager.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class EmployeesDto(
    val id: Int,
    val roleName: String,
    val name: String,
    val email: String,
    val phone: String,
    val schedules: List<ScheduleDto>
)