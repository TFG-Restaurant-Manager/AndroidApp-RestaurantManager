package com.tfg_rm.androidapp_restaurantmanager.domain.viewmodels

import androidx.compose.runtime.mutableStateListOf
import com.tfg_rm.androidapp_restaurantmanager.R
import java.time.Duration
import java.time.LocalDateTime
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.tfg_rm.androidapp_restaurantmanager.domain.models.Dishes
import com.tfg_rm.androidapp_restaurantmanager.domain.models.Order
import com.tfg_rm.androidapp_restaurantmanager.domain.models.OrderItem

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
                OrderItem(id = 1,dish = Dishes(101,"Ensalada César","Ensalada",
                    "Entrantes",10.5,true),quantity =  2, null),
                OrderItem(2, dish = Dishes(102,"Solomillo al Roquefort","Carne",
                    "Principales",15.00,true),2, "Punto medio"),
                OrderItem(3, dish = Dishes(103,"Vino Tinto Crianza","Alcohol",
                    "Bebidas",10.00,true), 1, null)
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
                OrderItem(4, dish = Dishes(103,"Paella Valenciana","Muy Rica",
                    "Principales",15.00,true), 1, null),
                OrderItem(5, dish = Dishes(103,"Agua Mineral","Muy Rica",
                    "Bebidas",3.00,true),  2,  null)
            )
        )

        ordersList.add(order1)
        ordersList.add(order2)
        ordersList.add(order2)
        ordersList.add(order2)
        ordersList.add(order2)

    }

    fun getStatusStringRes(statusId: Int) = when (statusId) {
       1 -> R.string.order_statuscreated
       2 -> R.string.order_statuscooked
       3 -> R.string.order_statusready
       4 -> R.string.order_statusdelivered
       5 -> R.string.order_statuspaid
       else -> R.string.order_statuserror
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
