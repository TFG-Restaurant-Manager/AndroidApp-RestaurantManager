package com.tfg_rm.androidapp_restaurantmanager.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class OrderItemDto(
    val orderItemId: Int,
    val dishId: Int,
    val dishName: String,
    val orderItemPrice: Double,
    val itemNotes: String?
)
