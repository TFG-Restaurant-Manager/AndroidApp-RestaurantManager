package com.tfg_rm.androidapp_restaurantmanager.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "order_items",
    foreignKeys = [
        ForeignKey(
            entity = Orders::class,
            parentColumns = ["id"],
            childColumns = ["orderId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Dishes::class,
            parentColumns = ["id"],
            childColumns = ["dishId"]
        )
    ],
    indices = [Index("orderId"), Index("dishId")]
)
data class OrderItems(
    @PrimaryKey
    val id: Int,
    val orderId: Int,
    val dishId: Int,
    val unitPrice: Double,
    val notes: String
)
