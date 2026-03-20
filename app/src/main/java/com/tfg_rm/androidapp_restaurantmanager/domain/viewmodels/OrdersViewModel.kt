package com.tfg_rm.androidapp_restaurantmanager.domain.viewmodels

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfg_rm.androidapp_restaurantmanager.R
import com.tfg_rm.androidapp_restaurantmanager.domain.models.Order
import com.tfg_rm.androidapp_restaurantmanager.domain.models.UiState
import com.tfg_rm.androidapp_restaurantmanager.domain.services.OrderService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(
    private val orderService: OrderService
) : ViewModel() {
    private val _orders = MutableStateFlow<UiState<MutableList<Order>>>(UiState.Idle)
    val orders = _orders.asStateFlow()

    fun getOrders() {
        viewModelScope.launch {
            _orders.value = UiState.Loading
            try {
                delay(1200)
                val ordenes = orderService.getOrders()
                _orders.value = UiState.Success(ordenes)
            } catch (e: Exception) {
                Log.e("OrdersViewModel", e.message ?: "Error al cargar las ordenes")
                _orders.value = UiState.Error(R.string.order_geterror)
            }
        }
    }

    fun removeOrderById(orderId: Int) {
        (_orders.value as UiState.Success).data.removeAll { it.id == orderId }
    }

    fun getStatusStringRes(status: Int) = when (status) {
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
            1 -> Color(0xFFE3F2FD) to Color(0xFF1976D2)
            2 -> Color(0xFFE8F5E9) to Color(0xFF2E7D32)
            else -> Color(0xFFF5F5F5) to Color(0xFF616161)
        }
    }
}
