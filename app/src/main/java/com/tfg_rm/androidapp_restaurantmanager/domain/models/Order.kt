package com.tfg_rm.androidapp_restaurantmanager.domain.models

import java.time.LocalDateTime


data class Order(
    // ── Server fields (DB: orders) ───────────────────────────────────────
    val id: Int,
    val tableId: Int?,
    val tableName: String?,
    val type: String?,
    val status: String,          // CREATED | COOKED | DELIVERED | PAID
    val total: Double,
    val orderType: String = "TABLE",  // TABLE | WEB (DB: order_type_id)
    val notes: String? = null,
    val clientId: Int? = null,
    val deliveryAddress: String? = null,
    val deliveryNotes: String? = null,
    val pickupTime: LocalDateTime? = null,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val orderItemsList: MutableList<OrderItem> = mutableListOf()
)