package com.tfg_rm.androidapp_restaurantmanager.data.remote.mapper

import com.tfg_rm.androidapp_restaurantmanager.data.remote.dto.DishesDto
import com.tfg_rm.androidapp_restaurantmanager.domain.models.Dishes

/**
 * Extension of the [DishesDto] model to facilitate conversion to a [Dishes] domain object.
 *
 * This mapping function decouples the data layer (DTO) from the domain layer, transforming
 * the structure received from the API into a business model usable by the application's logic.
 *
 * @return A [Dishes] instance with the corresponding data extracted from the DTO.
 */
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