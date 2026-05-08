package com.tfg_rm.androidapp_restaurantmanager.data.remote.dto

import kotlinx.serialization.Serializable

/**
 * Data Transfer Object (DTO) representing a combined view of a table and its active order.
 *
 * This class is useful for the user interface as it consolidates the physical information
 * of the table with the details of the order currently associated with it, allowing
 * for more efficient management from the table map or order list.
 *
 * @property tableId Unique identifier of the table.
 * @property tableName Name or descriptive label of the table (e.g., "Table 1").
 * @property capacity Maximum number of diners.
 * @property posX X-coordinate for graphical representation.
 * @property posY Y-coordinate for graphical representation.
 * @property status Current status of the table (e.g., "FREE", "OCCUPIED").
 * @property sectionTitle Name of the sector or zone where the table is located.
 * @property orderId Identifier of the active order, or null if the table has no orders.
 * @property orderStatus Status of the associated order (e.g., "IN_PREPARATION", "DELIVERED").
 * @property orderTotal Accumulated total amount of the current order.
 * @property orderNotes General comments or notes associated with the active ticket.
 * @property orderCreatedAt Date and time the order was started at the table.
 * @property orderItems List of products requested in the current order, or null if empty.
 */
@Serializable
data class TablesOrdersDto(
    val tableId: Int,
    val tableName: String,
    val capacity: Int,
    val posX: Double,
    val posY: Double,
    val status: String,
    val sectionTitle: String,
    val orderId: Int?,
    val orderStatus: String?,
    val orderTotal: Double?,
    val orderNotes: String?,
    val orderCreatedAt: String?,
    val orderItems: List<OrderItemDto>?
)