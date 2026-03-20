package com.tfg_rm.androidapp_restaurantmanager.data.remote.dto

import java.time.LocalDateTime

data class OrderDto(
    val id: Int,
    val tableId: Int,
    val clientId: Int,
    val statusId: Int,
    val total: Float,
    val notes: String?,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val orderTypeId: Int,
    val deliveryAddress: String?,
    val deliveryNotes: String?,
    val orderItemsList: MutableList<OrderItemDto> = mutableListOf()
)
