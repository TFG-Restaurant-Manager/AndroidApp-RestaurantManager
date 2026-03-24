package com.tfg_rm.androidapp_restaurantmanager.domain.models

import java.time.LocalDateTime

data class Order(
    val id: Int,
    val tableId: Int,
    val status: String,
    var total: Double,
    val notes: String?,
    val createdAt: LocalDateTime,
    val orderItemsList: MutableList<OrderItem> = mutableListOf()
)