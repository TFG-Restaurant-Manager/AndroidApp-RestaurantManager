package com.tfg_rm.androidapp_restaurantmanager.viewmodels

import androidx.compose.runtime.mutableStateListOf
import com.tfg_rm.androidapp_restaurantmanager.R
import com.tfg_rm.androidapp_restaurantmanager.data.models.Order
import com.tfg_rm.androidapp_restaurantmanager.data.models.OrderItem
import java.time.Duration
import java.time.LocalDateTime
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel

class OrdersViewModel() : ViewModel() {
    val ordersList = mutableStateListOf<Order>()

    init {
        // Mock data for preview and initial state
        val now = LocalDateTime.now()
        
        val order1 = Order(
            id = 1,
            restaurantId = 1,
            tableId = 2,
            clientId = 1,
            statusId = 2, // In kitchen
            total = 61.00f,
            notes = null,
            createdAt = now.minusMinutes(15),
            orderTypeId = 1,
            deliveryAddress = null,
            deliveryNotes = null,
            orderItemsList = mutableListOf(
                OrderItem(1, 101, "Ensalada César", 2, 10.50, null),
                OrderItem(2, 102, "Solomillo al Roquefort", 2, 15.00, "Punto medio"),
                OrderItem(3, 103, "Vino Tinto Crianza", 1, 10.00, null)
            )
        )

        val order2 = Order(
            id = 2,
            restaurantId = 1,
            tableId = 4,
            clientId = 2,
            statusId = 3, // Ready
            total = 21.00f,
            notes = null,
            createdAt = now.minusMinutes(25),
            orderTypeId = 1,
            deliveryAddress = null,
            deliveryNotes = null,
            orderItemsList = mutableListOf(
                OrderItem(4, 104, "Paella Valenciana", 1, 15.00, null),
                OrderItem(5, 105, "Agua Mineral", 2, 3.00, null)
            )
        )

        ordersList.add(order1)
        ordersList.add(order2)
        ordersList.add(order2)
        ordersList.add(order2)
        ordersList.add(order2)

    }

    fun getStatusStringRes(statusId: Int) = when (statusId) {
       1 -> R.string.orderStatusCreated
       2 -> R.string.orderStatusCooked
       3 -> R.string.orderStatusReady
       4 -> R.string.orderStatusDelivered
       5 -> R.string.orderStatusPaid
       else -> R.string.orderStatusError
   }

    fun getMinutesAgo(createdAt: LocalDateTime): Long {
        return Duration.between(createdAt, LocalDateTime.now()).toMinutes()
    }

    fun getStatusColors(statusId: Int): Pair<Color, Color> {
        return when (statusId) {
            2 -> Color(0xFFE3F2FD) to Color(0xFF1976D2)
            3 -> Color(0xFFE8F5E9) to Color(0xFF2E7D32)
            else -> Color(0xFFF5F5F5) to Color(0xFF616161)
        }
    }
}
