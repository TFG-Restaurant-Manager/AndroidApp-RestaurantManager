package com.tfg_rm.androidapp_restaurantmanager.controller

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.tfg_rm.androidapp_restaurantmanager.data.models.Dishes
import com.tfg_rm.androidapp_restaurantmanager.data.models.OrderItems
import com.tfg_rm.androidapp_restaurantmanager.data.models.Orders

class FoodController {
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
        val existing = order.value.orderDishes.firstOrNull { it.dishId == dish.id }
        if (existing != null) {
            existing.quantity++
        } else {
            val listOrderItemsIds = order.value.orderDishes.map { it.id }
            order.value.orderDishes.add(
                OrderItems(
                    (1..listOrderItemsIds.max())
                        .firstOrNull { it !in listOrderItemsIds }
                        ?: ((listOrderItemsIds.maxOrNull() ?: 0) + 1),
                    dish.id,
                    quantity = 1,
                    mutableStateOf("")
                )
            )
        }
    }

    fun plusDishOnOrder (order: MutableState<Orders>, dish: Dishes) {
        if (order.value.orderDishes.map { it.dishId }.contains(dish.id)) {
            order.value.orderDishes.first { it.dishId == dish.id }
                .quantity.plus(1)
        }
    }

    fun minusDishOnOrder (order: MutableState<Orders>, dish: Dishes) {
        if (order.value.orderDishes.map { it.dishId }.contains(dish.id)) {
            val orderDishLocation = order.value.orderDishes.first { it.dishId == dish.id }
            orderDishLocation.quantity.minus(1)
            if (orderDishLocation.quantity == 0) {
                order.value.orderDishes.remove(orderDishLocation)
            }
        }
    }
}