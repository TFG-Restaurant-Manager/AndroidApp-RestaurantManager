package com.tfg_rm.androidapp_restaurantmanager.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dishes")
data class Dishes(
    @PrimaryKey
    val id: Int,
    val name: String,
    val categoryName: String,
    val description: String,
    val price: Double,
    val available: Boolean
)