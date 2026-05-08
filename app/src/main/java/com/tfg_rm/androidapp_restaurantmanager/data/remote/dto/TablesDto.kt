package com.tfg_rm.androidapp_restaurantmanager.data.remote.dto

import kotlinx.serialization.Serializable

/**
 * Data Transfer Object (DTO) representing a physical table in the restaurant.
 *
 * Contains the necessary information to manage capacity, location within the premises,
 * and current occupancy status for visual representation on the restaurant map.
 *
 * @property id Unique identifier of the table in the system.
 * @property capacity Maximum number of diners that can be seated at the table.
 * @property section Name of the area or sector of the restaurant where it is located (e.g., "Terrace", "Main Hall").
 * @property posX X-coordinate for the graphical positioning of the table in the interface.
 * @property posY Y-coordinate for the graphical positioning of the table in the interface.
 * @property status Current status of the table (e.g., "FREE", "OCCUPIED", "RESERVED").
 */
@Serializable
data class TablesDto(
    val id: Int,
    val capacity: Int,
    val section: String,
    val posX: Double,
    val posY: Double,
    val status: String
)
