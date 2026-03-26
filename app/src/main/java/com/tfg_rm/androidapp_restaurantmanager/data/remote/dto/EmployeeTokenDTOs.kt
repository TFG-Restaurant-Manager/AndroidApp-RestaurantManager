package com.tfg_rm.androidapp_restaurantmanager.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class EmployeeTokenRequest(
    val code: String,
    val password: String
)

@Serializable
data class EmployeeTokenResponse(
    val token: String,
)
