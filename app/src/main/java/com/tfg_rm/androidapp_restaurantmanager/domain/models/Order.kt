package com.tfg_rm.androidapp_restaurantmanager.domain.models

import java.time.LocalDateTime

/**
 * Domain model representing a customer order within the restaurant.
 *
 * This class tracks the lifecycle of an order from its creation to its final total,
 * managing the relationship between the table, the items requested, and the current
 * processing status.
 *
 * @property id Unique identifier of the order.
 * @property tableId Identifier of the table associated with this order.
 * @property status Current state of the order (e.g., "PENDING", "PAID", "CANCELLED").
 * @property total The current accumulated monetary value of all items in the order.
 * @property notes Optional general comments or special instructions for the entire order.
 * @property createdAt The exact date and time the order was initially opened.
 * @property orderItemsList A dynamic list of [OrderItem] objects belonging to this order.
 */
data class Order(
    val id: Int,
    val tableId: Int,
    val status: String,
    var total: Double,
    val notes: String?,
    val createdAt: LocalDateTime,
    val orderItemsList: MutableList<OrderItem> = mutableListOf()
)