package com.tfg_rm.androidapp_restaurantmanager.data.remote.dto

import kotlinx.serialization.Serializable


@Serializable
data class EmployeeLoginRequest(
    val dni: String,
    val password: String
)

@Serializable
data class EmployeeLoginResponse(
    val employeeId: Long,
    val restaurantIds: List<Long>
)
