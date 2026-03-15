package com.tfg_rm.androidapp_restaurantmanager.domain.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.tfg_rm.androidapp_restaurantmanager.domain.services.OrderService
import com.tfg_rm.androidapp_restaurantmanager.R
import com.tfg_rm.androidapp_restaurantmanager.domain.models.Dishes
import com.tfg_rm.androidapp_restaurantmanager.domain.models.Order
import com.tfg_rm.androidapp_restaurantmanager.domain.models.OrderItem
import java.time.Duration
import java.time.LocalDateTime

@HiltViewModel
class OrdersViewModel @Inject constructor(
    private val orderService: OrderService
) : ViewModel() {
    val ordersList = mutableStateListOf<Order>()

    init {

        //ordersList.addAll(orderService.getOrders())

    }

    fun removeOrderById(orderId: Int) {
        ordersList.removeAll { it.id == orderId }
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
