package com.tfg_rm.androidapp_restaurantmanager.domain.models
import java.time.LocalDateTime

data class Order(
    val id: Int,
    val restaurantId: Int,
    val tableId: Int,
    val status: String,
    val total: Float,
    val notes: String?,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val orderItemsList: MutableList<OrderItem> = mutableListOf()
)