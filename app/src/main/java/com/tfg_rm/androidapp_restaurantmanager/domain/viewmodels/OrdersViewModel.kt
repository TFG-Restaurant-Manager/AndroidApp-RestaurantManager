package com.tfg_rm.androidapp_restaurantmanager.domain.viewmodels

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfg_rm.androidapp_restaurantmanager.R
import com.tfg_rm.androidapp_restaurantmanager.data.remote.dto.OrderResponse
import com.tfg_rm.androidapp_restaurantmanager.data.remote.dto.WebsocketMessage
import com.tfg_rm.androidapp_restaurantmanager.data.remote.mapper.toOrder
import com.tfg_rm.androidapp_restaurantmanager.domain.models.Order
import com.tfg_rm.androidapp_restaurantmanager.domain.models.OrderItem
import com.tfg_rm.androidapp_restaurantmanager.domain.models.UiState
import com.tfg_rm.androidapp_restaurantmanager.domain.services.OrderService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import java.time.Duration
import java.time.LocalDateTime
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

/**
 * ViewModel responsible for managing the state and business logic of customer orders.
 * * This class orchestrates the retrieval of active orders from the [OrderService] and
 * provides helper methods to process order status, elapsed time, and UI styling
 * (colors and strings) based on the order's state.
 *
 * @property service The domain service providing access to order-related operations and cache management.
 */
@HiltViewModel
class OrdersViewModel @Inject constructor(
    private val service: OrderService
) : ViewModel() {

    private val _orders = MutableStateFlow<UiState<MutableList<Order>>>(UiState.Idle)

    /**
     * Observable state flow representing the current state of the orders list.
     * Consumers can observe this to handle [UiState.Loading], [UiState.Success], or [UiState.Error].
     */
    val orders = _orders.asStateFlow()

    /**
     * Resets the internal UI state to [UiState.Idle] and clears the service cache.
     * This ensures that the next data fetch will retrieve fresh information from the remote source.
     */
    fun resetState() {
        socketJob?.cancel()
        _orders.value = UiState.Idle
    }

    /**
     * Fetches the current list of orders asynchronously.
     * It includes a small artificial delay to ensure smooth UI transitions and
     * handles errors by mapping them to specific string resources.
     */
    fun getOrders() {
        viewModelScope.launch {
            _orders.value = UiState.Loading
            try {
                val ordenes = service.getOrders()
                observeSocketMessages()
                _orders.value = UiState.Success(ordenes)
            } catch (e: Exception) {
                Log.e("OrdersViewModel", e.message ?: "Error loading orders")
                _orders.value = UiState.Error(R.string.order_geterror)
            }
        }
    }


    /**
     * Maps the technical status string from the backend to a localized string resource.
     * * @param status The status code (e.g., "CREATED", "COOKED").
     * @return The string resource ID for the display label.
     */
    fun getStatusStringRes(status: String) = when (status) {
        "CREATED" -> R.string.order_statuscreated
        "COOKED" -> R.string.order_statuscooked
        "DELIVERED" -> R.string.order_statusdelivered
        "PAID" -> R.string.order_statuspaid
        else -> R.string.order_statuserror
    }

    /**
     * Calculates the time elapsed since the order was created.
     * * @param createdAt The timestamp when the order was opened.
     * @return The number of minutes passed since creation.
     */
    fun getMinutesAgo(createdAt: LocalDateTime): Long {
        return Duration.between(createdAt, LocalDateTime.now()).toMinutes()
    }

    /**
     * Provides a color pair (Background, Content/Text) based on the order status.
     * Used to style status badges or chips in the UI.
     * * @param statusId The status identifier.
     * @return A [Pair] containing the [Color] for the background and the [Color] for the foreground.
     */
    fun getStatusColors(statusId: String): Pair<Color, Color> {
        return when (statusId) {
            "CREATED" -> Color(0xFFE3F2FD) to Color(0xFF1976D2) // Blue theme
            "COOKED" -> Color(0xFFE8F5E9) to Color(0xFF2E7D32)  // Green theme
            else -> Color(0xFFF5F5F5) to Color(0xFF616161)      // Grey theme (Default)
        }
    }

    fun updateOrderState(order: Order) {
        Log.i("OrdersViewModel", "Orden enviada completamente")
        viewModelScope.launch {
            try {
                service.updateOrderState(order.copy(status = "DELIVERED"))
            } catch (e: Exception) {
                Log.e("OrdersViewModel", e.message ?: "Error al actualizar el estado de la orden")
            }
        }
    }

    fun updateOrderItemState(order: Order, orderItemId: Int) {
        viewModelScope.launch {
            try {
                val orderUpdated = order.copy(
                    orderItemsList = order.orderItemsList.map {
                        if (it.orderItemId == orderItemId) {
                            it.copy(status = "DELIVERED")
                        } else it
                    } as MutableList<OrderItem>
                )
                service.updateOrderState(orderUpdated)
            } catch (e: Exception) {
                Log.e("OrdersViewModel", e.message ?: "Error al actualizar el estado de la orden")
            }
        }
    }

    private var socketJob: Job? = null

    private fun observeSocketMessages() {
        Log.i("OrdersViewModel", "Obserbando mensajes en OrdersViewModel")

        socketJob = viewModelScope.launch {

            service.observeMessages().collect { message ->
                Log.i("OrdersViewModel", "Mensaje recibido en OrdersViewModel: $message")
                try {
                    when {
                        message.contains("ORDER_CREATED") -> {
                            val result = Json.decodeFromString<WebsocketMessage>(message)
                            val newOrder =
                                Json.decodeFromJsonElement<OrderResponse>(result.payload).toOrder()
                            Log.i("OrdersViewModel", "Orden a añadir: $newOrder")
                            _orders.update { state ->
                                if (state is UiState.Success<MutableList<Order>>) {
                                    val list = state.data + newOrder
                                    UiState.Success(list.toMutableList())
                                } else state
                            }
                            Log.i("OrdersViewModel", "Mensaje websocket, orden creada")
                        }

                        message.contains("ORDER_UPDATED") -> {
                            val result = Json.decodeFromString<WebsocketMessage>(message)
                            val orderModified =
                                Json.decodeFromJsonElement<OrderResponse>(result.payload).toOrder()
                            Log.i("OrdersViewModel", "Orden a modificar: $orderModified")
                            _orders.update { state ->
                                if (state is UiState.Success) {
                                    UiState.Success(state.data.map { currentOrder ->
                                        if (currentOrder.id == orderModified.id) {
                                            orderModified
                                        } else {
                                            currentOrder
                                        }
                                    }.toMutableList())
                                } else state
                            }
                            Log.i("OrdersViewModel", "Mensaje websocket, orden modificada")
                        }

                        message.contains("FAILED_CREATE_ORDER") -> {
                            Log.i("OrdersViewModel", "Error al crear la orden FAILED_CREATE_ORDER")
                        }

                        message.contains("FAILED_UNHANDLED_MESSAGE ") -> {
                            Log.i(
                                "OrdersViewModel",
                                "Mensaje erroneo, no tiene formato del json requerido"
                            )
                        }

                        message.contains("FAILED_UNKNOWN_TYPE") -> {
                            Log.i("OrdersViewModel", "Error desconocido")
                        }
                    }
                } catch (_: CancellationException) {
                    Log.e("OrdersViewModel", "Hilo cerrado")
                    service.disconnectWS()
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.e("OrdersViewModel", e.message ?: "Excepcion no encontrada")
                }
            }

        }
    }
}
