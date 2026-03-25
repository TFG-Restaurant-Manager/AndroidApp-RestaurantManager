package com.tfg_rm.androidapp_restaurantmanager.data.remote.mapper

import com.tfg_rm.androidapp_restaurantmanager.data.remote.dto.OrderDto
import com.tfg_rm.androidapp_restaurantmanager.data.remote.dto.OrderItemDto
import com.tfg_rm.androidapp_restaurantmanager.domain.models.Order
import com.tfg_rm.androidapp_restaurantmanager.domain.models.OrderItem
import java.time.LocalDateTime

fun OrderDto.toOrder(): Order {
    return Order(
        id = this.id,
        tableId = this.tableId,
        status = this.statusId,
        total = this.total.toDouble(),
        notes = this.notes,
        createdAt = LocalDateTime.parse(this.createdAt),
        orderItemsList = this.orderItemsList.map { it.toOrderItem() }.toMutableList()
    )
}

fun OrderItemDto.toOrderItem(): OrderItem {
    return OrderItem(
        orderItemId = this.orderItemId,
        dishId = this.dishId,
        dishName = this.dishName,
        notes = this.itemNotes,
        price = this.orderItemPrice
    )
}