package com.tfg_rm.androidapp_restaurantmanager.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class OrderWithItemsEntity(

    @Embedded
    val order: OrdersEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "orderId"
    )
    val items: List<OrderItemsEntity>
)