package com.tfg_rm.androidapp_restaurantmanager.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class DishesDto(
    val id: Int,
    val name: String,
    val description: String,
    val categoryName: String,
    val price: Double,
    val available: Boolean
)
