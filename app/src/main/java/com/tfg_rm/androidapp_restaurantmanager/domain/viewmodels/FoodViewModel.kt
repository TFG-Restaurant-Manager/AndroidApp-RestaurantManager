package com.tfg_rm.androidapp_restaurantmanager.domain.viewmodels

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfg_rm.androidapp_restaurantmanager.R
import com.tfg_rm.androidapp_restaurantmanager.domain.models.Dishes
import com.tfg_rm.androidapp_restaurantmanager.domain.models.Order
import com.tfg_rm.androidapp_restaurantmanager.domain.models.OrderItems
import com.tfg_rm.androidapp_restaurantmanager.domain.models.Orders
import com.tfg_rm.androidapp_restaurantmanager.domain.models.UiState
import com.tfg_rm.androidapp_restaurantmanager.domain.services.FoodService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class FoodViewModel @Inject constructor(
    private val foodService: FoodService
) : ViewModel() {

    private val _dishes = MutableStateFlow<UiState<List<Dishes>>>(UiState.Idle)
    val dishes = _dishes.asStateFlow()


    fun getDishes() {
        viewModelScope.launch {
            _dishes.value = UiState.Loading
            try {
                val dishes = foodService.getDishes()
                _dishes.value = UiState.Success(dishes)
            } catch (e: Exception) {
                Log.e("FoodViewModel", e.message ?: "Error: getDishes FoodViewModel", e.cause)
                _dishes.value =
                    UiState.Error(R.string.foodscreen_error)
            }
        }
    }

    fun getDishesCategories(dishes: List<Dishes>): List<String> {
        return dishes.map { it.category }
            .distinct().let { listOf("Todo") + it }
    }

    fun getOrder(tableId: Int): Orders {
        return Orders(
            1, //Example or a order id, i a future it will need to be changed to the real id
            tableId,
            "Creado",
            0.toDouble(),
            LocalDateTime.now(),
            mutableStateListOf<OrderItems>()
        )
    }

    fun filterDishes(
        dishes: List<Dishes>,
        searchedDish: String,
        selectedCategory: String
    ): List<Dishes> {
        return dishes.filter { dish ->
            val matchesCategory =
                selectedCategory == "Todo" || dish.category == selectedCategory

            val matchesSearch =
                searchedDish.isBlank() ||
                        dish.name.contains(searchedDish, ignoreCase = true)

            matchesCategory && matchesSearch
        }
    }

    fun addDishToOrder(order: MutableState<Orders>, dish: Dishes) {
        val existing = order.value.orderDishes.firstOrNull { it == dish }
        if (existing != null) {
            existing.quantity.value++
        } else {
            val listOrderItemsIds = order.value.orderDishes.map { it.id }
            order.value.orderDishes.add(
                OrderItems(
                    id = (1..if (listOrderItemsIds.isEmpty()) 1 else listOrderItemsIds.max())
                        .firstOrNull { it !in listOrderItemsIds }
                        ?: ((listOrderItemsIds.maxOrNull() ?: 0) + 1),
                    dish = dish,
                    quantity = mutableIntStateOf(1),
                    notes = mutableStateOf("")
                )
            )
            order.value.total += dish.price
        }
    }

    fun plusDishOnOrder(order: MutableState<Orders>, dish: Dishes) {
        if (order.value.orderDishes.map { it.dish }.contains(dish)) {
            order.value.orderDishes.first { it.dish == dish }
                .let { it.quantity.value++ }
            order.value.total += dish.price
        }
    }

    fun minusDishOnOrder(order: MutableState<Orders>, dish: Dishes) {
        if (order.value.orderDishes.map { it.dish }.contains(dish)) {
            val orderDishLocation = order.value.orderDishes.first { it.dish == dish }
            orderDishLocation.let { it.quantity.value-- }
            order.value.total -= dish.price
            if (orderDishLocation.quantity.value == 0) {
                order.value.orderDishes.remove(orderDishLocation)
            }
        }
    }

    fun isDishInOrder(order: MutableState<Orders>, dish: Dishes): Boolean {
        return order.value.orderDishes.map { it.dish }
            .contains(dish)
                && order.value.orderDishes.first {
            it.dish == dish
        }.quantity.value > 0
    }

    fun getDishQuantityInOrder(order: MutableState<Orders>, dish: Dishes): Int {
        return order.value.orderDishes
            .first { it.dish == dish }.quantity.value
    }

    fun getOrderDishesQuantity(order: MutableState<Orders>): Int {
        return order.value.orderDishes.map { it.quantity }.sumOf { it.value }
    }

    fun getOrderTotalAmount(order: MutableState<Orders>): Double {
        return order.value.total
    }

    fun getNotes(dish: Dishes, order: MutableState<Orders>): String {
        return order.value.orderDishes
            .firstOrNull { it.dish == dish }
            ?.notes?.value
            ?: ""
    }

    fun isNoteEmpty(dish: Dishes, order: MutableState<Orders>): Boolean {
        return getNotes(dish, order).isEmpty()
    }

    fun updateNotes(dish: Dishes, newNote: String, order: MutableState<Orders>) {
        order.value.orderDishes
            .firstOrNull { it == dish }
            ?.notes?.value = newNote
    }

    fun saveOrder(order: Order) {
        foodService.saveOrder(order)
    }
}