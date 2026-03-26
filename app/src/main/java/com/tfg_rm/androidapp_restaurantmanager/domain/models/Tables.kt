package com.tfg_rm.androidapp_restaurantmanager.domain.models

data class Tables(
    val id: Int,
    val capacity: Int,
    val section: String,
    val posX: Double,
    val posY: Double,
    val status: String
)