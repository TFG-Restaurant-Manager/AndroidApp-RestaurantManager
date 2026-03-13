package com.tfg_rm.androidapp_restaurantmanager.domain.models

import androidx.compose.runtime.MutableState

data class OrderItems (
    val id: Int,
    val dish: Dishes,
    var quantity: MutableState<Int>,
    val notes: MutableState<String>
)