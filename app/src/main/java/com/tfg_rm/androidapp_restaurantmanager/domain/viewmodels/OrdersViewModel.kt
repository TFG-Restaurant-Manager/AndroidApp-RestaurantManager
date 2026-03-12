package com.tfg_rm.androidapp_restaurantmanager.domain.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.tfg_rm.androidapp_restaurantmanager.R
import com.tfg_rm.androidapp_restaurantmanager.domain.models.Dishes
import com.tfg_rm.androidapp_restaurantmanager.domain.models.Order
import com.tfg_rm.androidapp_restaurantmanager.domain.models.OrderItem
import java.time.Duration
import java.time.LocalDateTime

class OrdersViewModel() : ViewModel() {
    val ordersList = mutableStateListOf<Order>()

    init {
        // Mock data for preview and initial state
        val now = LocalDateTime.now()

        val order1 = Order(
            id = 1,
            restaurantId = 1,
            tableId = 2,
            status = "KITCHEN",
            total = 61.00f,
            notes = null,
            createdAt = now.minusMinutes(15),
            orderItemsList = mutableListOf(
                OrderItem(
                    id = 1, dish = Dishes(
                        101, "Ensalada César", "Ensalada",
                        "Entrantes", 10.5, true
                    ), quantity = 2, null
                ),
                OrderItem(
                    2, dish = Dishes(
                        102, "Solomillo al Roquefort", "Carne",
                        "Principales", 15.00, true
                    ), 2, "Punto medio"
                ),
                OrderItem(
                    3, dish = Dishes(
                        103, "Vino Tinto Crianza", "Alcohol",
                        "Bebidas", 10.00, true
                    ), 1, null
                )
            )
        )

        val order2 = Order(
            id = 2,
            restaurantId = 1,
            tableId = 4,
            status = "READY",
            total = 21.00f,
            notes = null,
            createdAt = now.minusMinutes(25),
            orderItemsList = mutableListOf(
                OrderItem(
                    4, dish = Dishes(
                        103, "Paella Valenciana", "Muy Rica",
                        "Principales", 15.00, true
                    ), 1, null
                ),
                OrderItem(
                    5, dish = Dishes(
                        103, "Agua Mineral", "Muy Rica",
                        "Bebidas", 3.00, true
                    ), 2, null
                )
            )
        )

        ordersList.add(order1)
        ordersList.add(order2)
        ordersList.add(order2)
        ordersList.add(order2)
        ordersList.add(order2)

    }

    fun getStatusStringRes(status: String) = when (status) {
        "CREATED" -> R.string.order_statuscreated
        "COOCKED" -> R.string.order_statuscooked
        "READY" -> R.string.order_statusready
        "DELIVERED" -> R.string.order_statusdelivered
        "PAID" -> R.string.order_statuspaid
        else -> R.string.order_statuserror
    }

    fun getMinutesAgo(createdAt: LocalDateTime): Long {
        return Duration.between(createdAt, LocalDateTime.now()).toMinutes()
    }

    fun getStatusColors(statusId: String): Pair<Color, Color> {
        return when (statusId) {
            "COOKED" -> Color(0xFFE3F2FD) to Color(0xFF1976D2)
            "READY" -> Color(0xFFE8F5E9) to Color(0xFF2E7D32)
            else -> Color(0xFFF5F5F5) to Color(0xFF616161)
        }
    }
}
