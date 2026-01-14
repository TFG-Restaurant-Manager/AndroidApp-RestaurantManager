package com.tfg_rm.androidapp_restaurantmanager.data.models

data class OrderItems (
    val id: Int,
    val dishId: Int,
    val quantity: Int,
    val notes: String
)