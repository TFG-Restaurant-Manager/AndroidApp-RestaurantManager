package com.tfg_rm.androidapp_restaurantmanager.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ScheduleDto(
    val id: Int,
    val startDatetime: String,
    val endDatetime: String
)