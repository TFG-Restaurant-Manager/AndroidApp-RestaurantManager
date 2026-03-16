package com.tfg_rm.androidapp_restaurantmanager.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class OrderWithItems(

    @Embedded
    val order: Orders,

    @Relation(
        parentColumn = "id",
        entityColumn = "orderId"
    )
    val items: List<OrderItems>
)