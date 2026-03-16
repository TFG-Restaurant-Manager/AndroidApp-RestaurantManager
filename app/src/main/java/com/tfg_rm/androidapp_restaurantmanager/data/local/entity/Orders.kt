package com.tfg_rm.androidapp_restaurantmanager.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class Orders(
    @PrimaryKey
    val id: Int,
    val tableId: Int,
    val orderStatus: String,
    val notes: String,
    val createdAt: Long,
)