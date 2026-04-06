package com.tfg_rm.androidapp_restaurantmanager.domain.models

/**
 * Domain model representing a physical table within the restaurant layout.
 *
 * This class is used to manage the seating arrangement, track occupancy, and
 * facilitate the visual positioning of tables in the application's digital
 * floor plan.
 *
 * @property id Unique identifier of the table.
 * @property capacity The maximum number of diners that can be seated at the table.
 * @property section The name of the restaurant area where the table is located (e.g., "Terrace", "Main Hall").
 * @property posX The horizontal coordinate used for positioning the table in the UI layout.
 * @property posY The vertical coordinate used for positioning the table in the UI layout.
 * @property status The current state of the table (e.g., "FREE", "OCCUPIED", "RESERVED").
 */
data class Tables(
    val id: Int,
    val capacity: Int,
    val section: String,
    val posX: Double,
    val posY: Double,
    val status: String
)