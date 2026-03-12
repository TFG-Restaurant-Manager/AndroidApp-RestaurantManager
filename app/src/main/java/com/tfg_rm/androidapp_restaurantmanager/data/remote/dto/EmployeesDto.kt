package com.tfg_rm.androidapp_restaurantmanager.data.remote.dto

import java.time.LocalDateTime

data class EmployeesDto(
    val id: Int,
    val roleName: String,
    val name: String,
    val email: String,
    val numberPhone: String,
    val dni: String,
    val workSchedules: List<Pair<LocalDateTime, LocalDateTime>>
)