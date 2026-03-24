package com.tfg_rm.androidapp_restaurantmanager.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class OrderItemDto(
    val id: Int,
    val dish: DishesDto,
    val dishName: String,
    val category: String,
    val quantity: Int,
    val unitPrice: Double,
    val notes: String? = null
)
