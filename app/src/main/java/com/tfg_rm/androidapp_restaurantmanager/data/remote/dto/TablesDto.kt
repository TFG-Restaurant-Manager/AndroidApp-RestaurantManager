package com.tfg_rm.androidapp_restaurantmanager.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class TablesDto(
    val id: Int,
    val capacity: Int,
    val section: String,
    val posX: Double,
    val posY: Double,
    val status: String
)
