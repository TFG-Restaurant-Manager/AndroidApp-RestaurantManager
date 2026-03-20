package com.tfg_rm.androidapp_restaurantmanager.data.remote.mapper

import com.tfg_rm.androidapp_restaurantmanager.data.remote.dto.OrderDto
import com.tfg_rm.androidapp_restaurantmanager.data.remote.dto.OrderItemDto
import com.tfg_rm.androidapp_restaurantmanager.data.remote.dto.OrderItemsDto
import com.tfg_rm.androidapp_restaurantmanager.data.remote.dto.OrdersDto
import com.tfg_rm.androidapp_restaurantmanager.domain.models.Order
import com.tfg_rm.androidapp_restaurantmanager.domain.models.OrderItem
import com.tfg_rm.androidapp_restaurantmanager.domain.models.OrderItems
import com.tfg_rm.androidapp_restaurantmanager.domain.models.Orders

fun OrderDto.toOrder(): Order {
    return Order(
        id = this.id,
        tableId = this.tableId,
        status = this.statusId,
        total = this.total,
        notes = this.notes,
        createdAt = this.createdAt,
        orderItemsList = this.orderItemsList.map { it.toOrderItem() }.toMutableList()
    )
}

fun OrdersDto.toOrders(): Orders {
    return Orders(
        id = this.id,
        tableId = this.tableId,
        status = this.status,
        total = this.total,
        createdAt = this.createdAt,
        orderDishes = this.orderDishes.map { it.toOrderItems() }.toMutableList()
    )
}

fun OrderItemsDto.toOrderItems(): OrderItems {
    return OrderItems(
        id = this.id,
        dish = this.dish.toDishes(),
        quantity = this.quantity,
        notes = this.notes
    )
}

fun OrderItemDto.toOrderItem(): OrderItem {
    return OrderItem(
        id = this.id,
        dish = this.dish.toDishes(),
        quantity = this.quantity,
        notes = this.notes
    )
}