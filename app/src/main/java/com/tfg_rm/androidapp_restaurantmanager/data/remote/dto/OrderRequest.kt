package com.tfg_rm.androidapp_restaurantmanager.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class OrderRequest(
    val id: Long? = null,
    val status: String,
    val type: String,           // "TABLE" | "DELIVERY" | "PICKUP"
    val tableId: Long? = null,
    val notes: String? = null,
    val createdAt: String,
    val deliveryAddress: String? = null,
    val deliveryNotes: String? = null,
    val items: List<OrderItemRequest>
)

@Serializable
data class OrderItemRequest(
    val id: Long,
    val dishId: Long,
    val notes: String? = null,
    val status: String
)