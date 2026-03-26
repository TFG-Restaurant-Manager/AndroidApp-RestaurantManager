package com.tfg_rm.androidapp_restaurantmanager.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "employees")
data class EmployeesEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val roleName: String,
    val email: String,
    val numberPhone: String,
    val dni: String
)