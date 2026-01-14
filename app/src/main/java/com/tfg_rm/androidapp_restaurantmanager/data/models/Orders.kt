package com.tfg_rm.androidapp_restaurantmanager.data.models

import java.time.LocalDate

data class Orders (
    val id : Int,
    val tableId: Int,
    val status: String,
    val total: Double,
    val createdAt: LocalDate,
    val orderDishes: MutableList<OrderItems>
)