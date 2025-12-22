package com.tfg_rm.androidapp_restaurantmanager.data.models

import java.time.LocalDateTime

data class Order(
    val id: Int,
    val restaurantId: Int,
    val tableId: Int,
    val clientId: Int,
    val statusId: Int,
    val total: Float,
    val notes: String?,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val orderTypeId: Int,
    val deliveryAddress: String?,
    val deliveryNotes: String?
)
