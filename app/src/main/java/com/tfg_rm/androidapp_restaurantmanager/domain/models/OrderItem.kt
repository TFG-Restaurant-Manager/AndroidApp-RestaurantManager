package com.tfg_rm.androidapp_restaurantmanager.domain.models

/**
 * Domain model representing an individual line item within a customer's order.
 *
 * This class links a specific dish to an order, capturing the point-of-sale details
 * such as the name and price at the time of purchase, along with any specific
 * customer requests or modifications.
 *
 * @property orderItemId Unique identifier for this specific entry in the order.
 * @property dishId Unique identifier of the reference dish from the menu.
 * @property dishName The name of the dish as it appears on the receipt or kitchen ticket.
 * @property price The unit price charged for this item.
 * @property notes Specific instructions or modifications for this item (e.g., "no onions", "extra spicy").
 */
data class OrderItem(
    val orderItemId: Int,
    val dishId: Int,
    val dishName: String,
    val price: Double,
    var notes: String?
)