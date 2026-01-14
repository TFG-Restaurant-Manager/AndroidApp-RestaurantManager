package com.tfg_rm.androidapp_restaurantmanager.data.models

import androidx.compose.runtime.MutableState

data class OrderItems (
    val id: Int,
    val dishId: Int,
    var quantity: Int,
    val notes: MutableState<String>
)