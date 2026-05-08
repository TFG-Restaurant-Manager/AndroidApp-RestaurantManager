package com.tfg_rm.androidapp_restaurantmanager.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class OrderResponse(
    val orderId: Int,
    val type: String,
    val status: String,
    val total: Double,
    val notes: String?,
    val createdAt: String,
    val items: List<OrderItemResponse>,
    val pickupTime: String?,
    val deliveryAddress: String?,
    val tableId: Int?,
    val tablName: String?
)

/**
 * Data Transfer Object (DTO) representing an individual item or line within an order.
 *
 * This class links a specific dish to a ticket, allowing for the recording of the price applied
 * at the time of purchase and any modifications or personalized notes for the kitchen.
 *
 * @property orderItemId Unique identifier of the order's detail line.
 * @property dishId Unique identifier of the dish associated with this line.
 * @property dishName Name of the requested dish.
 * @property orderItemPrice Unit price of the dish recorded at the time the order was placed.
 * @property itemNotes Specific observations for this item (e.g., "No salt", "Rare").
 * @property status Specific status for this item (e.g., "COOKED", "CREATED").
 */
@Serializable
data class OrderItemResponse(
    val orderItemId: Int,
    val dishId: Int,
    val dishName: String,
    val orderItemPrice: Double,
    val itemNotes: String?,
    val status: String
)