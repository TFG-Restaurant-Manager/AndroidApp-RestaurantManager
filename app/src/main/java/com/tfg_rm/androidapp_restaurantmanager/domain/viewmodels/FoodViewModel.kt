package com.tfg_rm.androidapp_restaurantmanager.domain.viewmodels

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfg_rm.androidapp_restaurantmanager.R
import com.tfg_rm.androidapp_restaurantmanager.domain.models.Dishes
import com.tfg_rm.androidapp_restaurantmanager.domain.models.Order
import com.tfg_rm.androidapp_restaurantmanager.domain.models.OrderItem
import com.tfg_rm.androidapp_restaurantmanager.domain.models.UiState
import com.tfg_rm.androidapp_restaurantmanager.domain.services.FoodService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FoodViewModel @Inject constructor(
    private val foodService: FoodService
) : ViewModel() {

    private val _dishes = MutableStateFlow<UiState<List<Dishes>>>(UiState.Idle)
    val dishes = _dishes.asStateFlow()

    fun resetState() {
        _dishes.value = UiState.Idle
    }


    fun getDishes() {
        viewModelScope.launch {
            _dishes.value = UiState.Loading
            try {
                val dishes = foodService.getDishes()
                _dishes.value = UiState.Success(dishes)
                Log.i("FoodViewModel", "Dishes recieved succesfully")
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

    fun addDishToOrder(order: MutableState<Order>, dish: Dishes) {
        val newList = order.value.orderItemsList.toMutableList()

        newList.add(
            OrderItem(
                orderItemId = (newList.maxOfOrNull { it.orderItemId } ?: 0) + 1,
                dishId = dish.id,
                dishName = dish.name,
                notes = "",
                price = dish.price
            )
        )

        order.value = order.value.copy(
            orderItemsList = newList,
            total = order.value.total + dish.price
        )
    }

//    fun plusDishOnOrder(order: MutableState<Order>, dish: Dishes) {
//        if (order.value.orderItemsList.map { it.dishId }.contains(dish.id)) {
//            order.value.orderItemsList.first { it.dishId == dish.id }
//                .let { it.quantity.intValue++ }
//            order.value.total += dish.price
//        }
//    }

    //    fun minusDishOnOrder(order: MutableState<Order>, dish: Dishes) {
//        if (order.value.orderItemsList.map { it.dishId }.contains(dish.id)) {
//            val orderDishLocation = order.value.orderItemsList.first { it.dishId == dish.id }
//            orderDishLocation.let { it.quantity.value-- }
//            order.value.total -= dish.price
//            if (orderDishLocation.quantity.value == 0) {
//                order.value.orderItemsList.remove(orderDishLocation)
//            }
//        }
//    }
    fun minusDishOnOrder(order: MutableState<Order>, dish: Dishes) {
        val newList = order.value.orderItemsList.toMutableList()
        val item = newList.firstOrNull { it.dishId == dish.id }

        if (item != null) {
            newList.remove(item)

            order.value = order.value.copy(
                orderItemsList = newList,
                total = order.value.total - dish.price
            )
        }
    }

    fun isDishInOrder(order: MutableState<Order>, dish: Dishes): Boolean {
        return order.value.orderItemsList.any { it.dishId == dish.id }
    }

    fun getDishQuantityInOrder(order: MutableState<Order>, dish: Dishes): Int {
        return order.value.orderItemsList.count { it.dishId == dish.id }
    }

    fun getOrderDishesQuantity(order: MutableState<Order>): Int {
        return order.value.orderItemsList.count()
    }

    fun getOrderTotalAmount(order: MutableState<Order>): Double {
        return order.value.total
    }

    fun getNotes(dish: Dishes, order: MutableState<Order>): String {
        return order.value.orderItemsList
            .firstOrNull { it.dishId == dish.id }
            ?.notes
            ?: ""
    }

    fun isNoteEmpty(dish: Dishes, order: MutableState<Order>): Boolean {
        return getNotes(dish, order).isEmpty()
    }

    fun updateNotes(dish: Dishes, newNote: String, order: MutableState<Order>) {
        order.value.orderItemsList
            .filter { it.dishId == dish.id }
            .forEach { it.notes = newNote }
    }

    fun saveOrder(order: Order) {
        foodService.saveOrder(order)
    }
}