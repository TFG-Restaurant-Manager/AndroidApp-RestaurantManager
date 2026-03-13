package com.tfg_rm.androidapp_restaurantmanager.domain.models

import androidx.compose.runtime.snapshots.SnapshotStateList
import java.time.LocalDateTime

data class Orders (
    val id : Int,
    val tableId: Int,
    val status: String,
    var total: Double,
    val createdAt: LocalDateTime,
    val orderDishes: MutableList<OrderItems>
)
