package com.tfg_rm.androidapp_restaurantmanager.domain.models

import java.time.LocalDateTime

data class Employee(
    val roleName: String,
    val name: String,
    val email: String,
    val phone: String,
    val schedules: List<Pair<LocalDateTime, LocalDateTime>>
)