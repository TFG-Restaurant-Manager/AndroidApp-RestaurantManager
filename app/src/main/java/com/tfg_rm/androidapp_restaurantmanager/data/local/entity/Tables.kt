package com.tfg_rm.androidapp_restaurantmanager.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tables")
data class Tables(
    @PrimaryKey
    val id: Int,
    val sectionName: String,
    val capacity: Int,
    val posX: Int,
    val posY: Int,
    val status: String
)