package com.tfg_rm.androidapp_restaurantmanager.data.remote.dto

import java.time.LocalDateTime

data class OrdersDto (
    val id : Int,
    val tableId: Int,
    val status: String,
    var total: Double,
    val createdAt: LocalDateTime,
    val orderDishes: List<OrderItemsDto>
)