package com.tfg_rm.androidapp_restaurantmanager.data.models

data class OrderItem(
    val id: Int,
    val dishId: Int,
    val dishName: String,
    val quantity: Int,
    val unitPrice: Double,
    val notes: String? = null
)
