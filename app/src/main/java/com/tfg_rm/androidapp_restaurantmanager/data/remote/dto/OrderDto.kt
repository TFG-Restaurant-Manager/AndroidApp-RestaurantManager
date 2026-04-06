package com.tfg_rm.androidapp_restaurantmanager.data.remote.dto

import kotlinx.serialization.Serializable

/**
 * Data Transfer Object (DTO) representing an order in the system.
 *
 * It contains all detailed information of a ticket, including the customer, the table,
 * the payment or preparation status, and the breakdown of the requested products.
 *
 * @property id Unique identifier of the order.
 * @property tableId Identifier of the associated table.
 * @property clientId Identifier of the customer placing the order.
 * @property statusId Identifier or key of the current order status (e.g., "PENDING", "PAID").
 * @property total Accumulated total amount of the order.
 * @property notes General observations or clarifications about the order (optional).
 * @property createdAt Date and time the order was created in string format.
 * @property orderTypeId Identifier of the order type (e.g., Dine-in, Pickup, Delivery).
 * @property deliveryAddress Destination address for delivery orders (optional).
 * @property deliveryNotes Specific instructions for the delivery driver (optional).
 * @property orderItemsList List of individual products that make up the order.
 */
@Serializable
data class OrderDto(
    val id: Int,
    val tableId: Int,
    val clientId: Int,
    val statusId: String,
    val total: Float,
    val notes: String?,
    val createdAt: String,
    val orderTypeId: Int,
    val deliveryAddress: String?,
    val deliveryNotes: String?,
    val orderItemsList: MutableList<OrderItemDto> = mutableListOf()
)
