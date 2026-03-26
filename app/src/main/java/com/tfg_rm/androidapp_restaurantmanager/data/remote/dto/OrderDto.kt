package com.tfg_rm.androidapp_restaurantmanager.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class OrderDto(
    val id: Int,
    val tableId: Int,
    val clientId: Int,
    val statusId: String,
    val total: Float,
    val notes: String?,
    val createdAt: String,
    val orderTypeId: Int,
    val deliveryAddress: String?,
    val deliveryNotes: String?,
    val orderItemsList: MutableList<OrderItemDto> = mutableListOf()
)
