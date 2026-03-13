package com.tfg_rm.androidapp_restaurantmanager.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class EmployeeTokenRequest(
    val dni : String,
    val password : String,
    val employeeId : Long,
    val restaurantId : Long
)

@Serializable
data class EmployeeTokenResponse(
    val token : String,
)
