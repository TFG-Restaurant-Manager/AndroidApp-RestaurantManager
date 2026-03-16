package com.tfg_rm.androidapp_restaurantmanager.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "work_schedules")
data class WorkSchedules(
    @PrimaryKey
    val id: Int,
    val start: String,
    val end: String
)