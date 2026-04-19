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

/**
 * ViewModel responsible for managing the menu (dishes) and the construction of active orders.
 *
 * This class handles the logic for fetching the menu, filtering items by category or search query,
 * and managing the temporary state of an order being edited in the UI. It bridges the view
 * with [FoodService].
 *
 * @property foodService The domain service providing access to food data and order persistence.
 */
@HiltViewModel
class FoodViewModel @Inject constructor(
    private val foodService: FoodService
) : ViewModel() {

    private val _dishes = MutableStateFlow<UiState<List<Dishes>>>(UiState.Idle)

    /**
     * Observable state flow containing the list of available dishes or the current loading/error status.
     */
    val dishes = _dishes.asStateFlow()

    /**
     * Resets the dishes state to [UiState.Idle].
     */
    fun resetState() {
        _dishes.value = UiState.Idle
    }

    /**
     * Fetches the menu items from the service and updates the UI state.
     */
    fun getDishes() {
        viewModelScope.launch {
            _dishes.value = UiState.Loading
            try {
                val dishes = foodService.getDishes()
                _dishes.value = UiState.Success(dishes)
                Log.i("FoodViewModel", "Dishes received successfully")
            } catch (e: Exception) {
                Log.e("FoodViewModel", e.message ?: "Error in FoodViewModel getDishes", e.cause)
                _dishes.value = UiState.Error(R.string.foodscreen_error)
            }
        }
    }

    /**
     * Extracts unique categories from the dish list and adds a "Todo" (All) default option.
     */
    fun getDishesCategories(dishes: List<Dishes>): List<String> {
        return dishes.map { it.category }
            .distinct().let { listOf("Todo") + it }
    }

    /**
     * Filters the provided list of dishes based on a search string and a selected category.
     */
    fun filterDishes(
        dishes: List<Dishes>,
        searchedDish: String,
        selectedCategory: String
    ): List<Dishes> {
        return dishes.filter { dish ->
            val matchesCategory = selectedCategory == "Todo" || dish.category == selectedCategory
            val matchesSearch =
                searchedDish.isBlank() || dish.name.contains(searchedDish, ignoreCase = true)
            matchesCategory && matchesSearch
        }
    }

    /**
     * Adds a specific dish to the current order state.
     * It handles ID generation for the new [OrderItem] and updates the total price.
     */
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

    /**
     * Removes one instance of a specific dish from the order and updates the total price.
     */
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

    /**
     * Checks if at least one instance of the dish exists in the current order.
     */
    fun isDishInOrder(order: MutableState<Order>, dish: Dishes): Boolean {
        return order.value.orderItemsList.any { it.dishId == dish.id }
    }

    /**
     * Returns the number of times a specific dish has been added to the order.
     */
    fun getDishQuantityInOrder(order: MutableState<Order>, dish: Dishes): Int {
        return order.value.orderItemsList.count { it.dishId == dish.id }
    }

    /**
     * Returns the total count of items currently in the order.
     */
    fun getOrderDishesQuantity(order: MutableState<Order>): Int {
        return order.value.orderItemsList.count()
    }

    /**
     * Returns the total monetary amount of the order.
     */
    fun getOrderTotalAmount(order: MutableState<Order>): Double {
        return order.value.total
    }

    /**
     * Retrieves the specific kitchen notes for a dish in the current order.
     */
    fun getNotes(dish: Dishes, order: MutableState<Order>): String {
        return order.value.orderItemsList
            .firstOrNull { it.dishId == dish.id }
            ?.notes ?: ""
    }

    /**
     * Checks if the kitchen notes for a specific dish are empty.
     */
    fun isNoteEmpty(dish: Dishes, order: MutableState<Order>): Boolean {
        return getNotes(dish, order).isEmpty()
    }

    /**
     * Updates the kitchen notes for all instances of a specific dish within the order.
     */
    fun updateNotes(dish: Dishes, newNote: String, order: MutableState<Order>) {
        order.value.orderItemsList
            .filter { it.dishId == dish.id }
            .forEach { it.notes = newNote }
    }

    /**
     * Persists the current order state via the food service.
     */
    fun saveOrder(order: Order) {
        foodService.saveOrder(order)
    }
}