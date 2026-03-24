package com.tfg_rm.androidapp_restaurantmanager.domain.models

import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState

data class OrderItem(
    val dishName: String,
    var quantity: MutableIntState,
    val price: Double,
    val category: String,
    val notes: MutableState<String>? = null
)