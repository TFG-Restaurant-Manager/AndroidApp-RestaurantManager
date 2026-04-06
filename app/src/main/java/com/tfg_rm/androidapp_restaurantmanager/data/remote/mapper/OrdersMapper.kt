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
        orderItemsList = this.orderItems.map { it.toOrderItem() }.toMutableList()
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

fun Order.toOrderDto(): OrderDto {
    return OrderDto(
        id = this.id,
        tableId = this.tableId,
        statusId = this.status,
        total = this.total.toFloat(),
        notes = this.notes,
        createdAt = this.createdAt.toString(),
        orderItems = this.orderItemsList.map { it.toOrderItemDto() }
    )
}

fun OrderItem.toOrderItemDto(): OrderItemDto {
    return OrderItemDto(
        orderItemId = this.orderItemId,
        dishId = this.dishId,
        dishName = this.dishName,
        orderItemPrice = this.price,
        itemNotes = this.notes
    )
}