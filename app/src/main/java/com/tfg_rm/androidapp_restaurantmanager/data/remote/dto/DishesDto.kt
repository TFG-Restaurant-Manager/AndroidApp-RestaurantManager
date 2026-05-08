package com.tfg_rm.androidapp_restaurantmanager.data.remote.dto

import kotlinx.serialization.Serializable

/**
 * Data Transfer Object (DTO) representing a food dish.
 *
 * This class is used to map the JSON response from the remote API regarding
 * menu dishes into the application's data model.
 *
 * @property id Unique identifier of the dish in the database.
 * @property name Name or title of the dish.
 * @property description Brief description of the ingredients or preparation of the dish.
 * @property categoryName Name of the category it belongs to (e.g., Starters, Drinks, Desserts).
 * @property price Retail price of the dish.
 * @property available Current availability status of the dish in the inventory/kitchen.
 */
@Serializable
data class DishesDto(
    val id: Int,
    val name: String,
    val description: String,
    val categoryName: String,
    val price: Double,
    val available: Boolean
)
