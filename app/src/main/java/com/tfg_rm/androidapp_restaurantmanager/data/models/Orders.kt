package com.tfg_rm.androidapp_restaurantmanager.data.models

import androidx.compose.runtime.snapshots.SnapshotStateList
import java.time.LocalDate
import java.time.LocalDateTime

data class Orders (
    val id : Int,
    val tableId: Int,
    val status: String,
    val total: Double,
    val createdAt: LocalDateTime,
    val orderDishes: SnapshotStateList<OrderItems>
)