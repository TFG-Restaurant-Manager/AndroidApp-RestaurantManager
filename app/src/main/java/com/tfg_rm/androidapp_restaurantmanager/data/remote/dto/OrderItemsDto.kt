package com.tfg_rm.androidapp_restaurantmanager.data.remote.dto

import androidx.compose.runtime.MutableState

data class OrderItemsDto(
    val id: Int,
    val dish: DishesDto,
    var quantity: MutableState<Int>,
    val notes: MutableState<String>
)