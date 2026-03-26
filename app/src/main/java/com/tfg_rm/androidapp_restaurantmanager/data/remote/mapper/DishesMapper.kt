package com.tfg_rm.androidapp_restaurantmanager.data.remote.mapper

import com.tfg_rm.androidapp_restaurantmanager.data.remote.dto.DishesDto
import com.tfg_rm.androidapp_restaurantmanager.domain.models.Dishes

fun DishesDto.toDishes(): Dishes {
    return Dishes(
        id = this.id,
        name = this.name,
        description = this.description,
        category = this.categoryName,
        price = this.price,
        available = this.available
    )
}