package com.tfg_rm.androidapp_restaurantmanager.data.remote.dto

import androidx.compose.runtime.MutableState

data class OrderItems (
    val id: Int,
    val dishId: Int,
    var quantity: MutableState<Int>,
    val notes: MutableState<String>
)