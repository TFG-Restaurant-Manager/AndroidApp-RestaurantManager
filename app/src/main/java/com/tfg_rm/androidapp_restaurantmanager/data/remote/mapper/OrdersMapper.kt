package com.tfg_rm.androidapp_restaurantmanager.data.remote.mapper

import com.tfg_rm.androidapp_restaurantmanager.data.remote.dto.OrderDto
import com.tfg_rm.androidapp_restaurantmanager.data.remote.dto.OrderItemDto
import com.tfg_rm.androidapp_restaurantmanager.domain.models.Order
import com.tfg_rm.androidapp_restaurantmanager.domain.models.OrderItem
import java.time.LocalDateTime

/**
 * Extension of the [OrderDto] model to facilitate conversion to an [Order] domain object.
 *
 * This mapping function transforms the order data received from the API into a domain model,
 * performing necessary type conversions such as parsing date strings into [LocalDateTime]
 * and mapping the list of order items using [toOrderItem].
 *
 * @return An [Order] instance with the processed data.
 */
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

/**
 * Extension of the [OrderItemDto] model to facilitate conversion to an [OrderItem] domain object.
 *
 * This function maps individual line items from their data transfer format (DTO)
 * into a domain model usable by the business logic.
 *
 * @return An [OrderItem] instance with the corresponding item details.
 */
fun OrderItemDto.toOrderItem(): OrderItem {
    return OrderItem(
        orderItemId = this.orderItemId,
        dishId = this.dishId,
        dishName = this.dishName,
        notes = this.itemNotes,
        price = this.orderItemPrice
    )
}