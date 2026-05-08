package com.tfg_rm.androidapp_restaurantmanager.domain.models

/**
 * Domain model representing a dish or product available in the restaurant's menu.
 *
 * This class is used by the business logic and the UI to display product information,
 * manage categories, and check for item availability.
 *
 * @property id Unique identifier of the dish.
 * @property name The display name of the dish (e.g., "Beef Burger").
 * @property description A brief explanation of the ingredients or preparation.
 * @property category The group or section this dish belongs to (e.g., "Starters", "Drinks").
 * @property price The sales price of the dish.
 * @property available Boolean flag indicating if the dish is currently in stock or can be ordered.
 */
data class Dishes(
    val id: Int,
    val name: String,
    val description: String,
    val category: String,
    val price: Double,
    val available: Boolean
)