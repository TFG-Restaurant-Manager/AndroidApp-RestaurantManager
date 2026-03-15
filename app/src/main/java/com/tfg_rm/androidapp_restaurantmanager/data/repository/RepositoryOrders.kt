package com.tfg_rm.androidapp_restaurantmanager.data.repository

import com.tfg_rm.androidapp_restaurantmanager.domain.models.Dishes
import com.tfg_rm.androidapp_restaurantmanager.domain.models.Order
import com.tfg_rm.androidapp_restaurantmanager.domain.models.OrderItem
import com.tfg_rm.androidapp_restaurantmanager.domain.models.Orders
import com.tfg_rm.androidapp_restaurantmanager.domain.models.OrderItems
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepositoryOrders @Inject constructor() {

    private val orderList: MutableList<Order> = mutableListOf()
    init {
        val now = LocalDateTime.now()
        orderList.add(
            Order(
                id = 1,
                restaurantId = 1,
                tableId = 2,
                status = "KITCHEN",
                total = 61.00f,
                notes = null,
                createdAt = now.minusMinutes(15),
                orderItemsList = mutableListOf(
                    OrderItem(1, Dishes(101, "Ensalada César", "Ensalada", "Entrantes", 10.5, true), 2, null),
                    OrderItem(2, Dishes(102, "Solomillo al Roquefort", "Carne", "Principales", 15.00, true), 2, "Punto medio"),
                    OrderItem(3, Dishes(103, "Vino Tinto Crianza", "Alcohol", "Bebidas", 10.00, true), 1, null)
                )
            )
        )

        orderList.add(
            Order(
                id = 2,
                restaurantId = 1,
                tableId = 4,
                status = "READY",
                total = 61.00f,
                notes = null,
                createdAt = now.minusMinutes(15),
                orderItemsList = mutableListOf(
                    OrderItem(4, Dishes(103, "Paella Valenciana", "Muy Rica", "Principales", 15.00, true), 1, null),
                    OrderItem(5, Dishes(112, "Agua Mineral", "Botella", "Bebidas", 3.00, true), 2, null)
                )
            )
        )
    }

    suspend fun getOrders(): MutableList<Order> {
        return orderList
    }

    // In-memory pending Orders per table (used by Food flow)


    fun saveOrder(order: Orders) {
        //dddd
    }
}
