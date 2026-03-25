package com.tfg_rm.androidapp_restaurantmanager.domain.models

data class OrderItem(
    val orderItemId: Int,
    val dishId: Int,
    val dishName: String,
    val price: Double,
    var notes: String?
)