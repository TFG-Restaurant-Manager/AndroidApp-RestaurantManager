package com.tfg_rm.androidapp_restaurantmanager.controller

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.navigation.NavController
import com.tfg_rm.androidapp_restaurantmanager.data.remote.dto.Dishes
import com.tfg_rm.androidapp_restaurantmanager.data.remote.dto.OrderItems
import com.tfg_rm.androidapp_restaurantmanager.data.remote.dto.Orders
import java.time.LocalDateTime

class FoodController {

    fun getDishes (): List<Dishes> {
        return listOf(
            Dishes(1, "Puré de patata", "Patatas cocidas con varios condimentos", "Principales", 8.5, true),
            Dishes(2, "Ensalada César", "Lechuga romana con pollo, crutones y salsa césar", "Entrantes", 7.5, true),
            Dishes(3, "Croquetas caseras", "Croquetas cremosas de jamón", "Entrantes", 6.0, true),
            Dishes(4, "Carpaccio de ternera", "Finas láminas de ternera con parmesano y aceite de oliva", "Entrantes", 9.0, true),
            Dishes(5, "Solomillo al Roquefort", "Solomillo de ternera con salsa de queso Roquefort", "Principales", 18.5, true),
            Dishes(6, "Lubina a la sal", "Lubina horneada con costra de sal", "Principales", 17.0, true),
            Dishes(7, "Paella valenciana", "Paella tradicional de pollo y verduras", "Principales", 15.0, true),
            Dishes(8, "Risotto de setas", "Arroz cremoso con setas y parmesano", "Principales", 14.0, true),
            Dishes(9, "Tarta de queso", "Tarta cremosa de queso al horno", "Postres", 5.5, true),
            Dishes(10, "Tiramisú", "Postre italiano con café y cacao", "Postres", 6.0, true),
            Dishes(11, "Coulant de chocolate", "Bizcocho de chocolate con corazón fundente", "Postres", 6.5, true),
            Dishes(12, "Agua mineral", "Botella de agua mineral", "Bebidas", 2.0, true),
            Dishes(13, "Vino tinto crianza", "Copa de vino tinto crianza", "Bebidas", 4.5, true),
            Dishes(14, "Cerveza", "Cerveza fría", "Bebidas", 3.0, true)
        )
    }

    fun getDishesCategories (dishes: List<Dishes>): List<String> {
        return dishes.map { it.category }
            .distinct().let { listOf("Todo") + it }
    }
    fun getOrder (tableId: Int): Orders {
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
        val existing = order.value.orderDishes.firstOrNull { it.dishId == dish.id }
        if (existing != null) {
            existing.quantity.value++
        } else {
            val listOrderItemsIds = order.value.orderDishes.map { it.id }
            order.value.orderDishes.add(
                OrderItems(
                    id = (1..if (listOrderItemsIds.isEmpty()) 1 else listOrderItemsIds.max())
                        .firstOrNull { it !in listOrderItemsIds }
                        ?: ((listOrderItemsIds.maxOrNull() ?: 0) + 1),
                    dishId = dish.id,
                    quantity = mutableIntStateOf(1),
                    notes = mutableStateOf("")
                )
            )
            order.value.total += dish.price
        }
    }

    fun plusDishOnOrder (order: MutableState<Orders>, dish: Dishes) {
        if (order.value.orderDishes.map { it.dishId }.contains(dish.id)) {
            order.value.orderDishes.first { it.dishId == dish.id }
                .let { it.quantity.value++ }
            order.value.total += dish.price
        }
    }

    fun minusDishOnOrder (order: MutableState<Orders>, dish: Dishes) {
        if (order.value.orderDishes.map { it.dishId }.contains(dish.id)) {
            val orderDishLocation = order.value.orderDishes.first { it.dishId == dish.id }
            orderDishLocation.let { it.quantity.value-- }
            order.value.total -= dish.price
            if (orderDishLocation.quantity.value == 0) {
                order.value.orderDishes.remove(orderDishLocation)
            }
        }
    }

    fun isDishInOrder (order: MutableState<Orders>, dishId: Int): Boolean {
        return order.value.orderDishes.map { it.dishId }
            .contains(dishId)
                && order.value.orderDishes.first {
            it.dishId == dishId
        }.quantity.value > 0
    }

    fun getDishQuantityInOrder (order: MutableState<Orders>, dishId: Int): Int {
        return order.value.orderDishes
            .first { it.dishId == dishId }.quantity.value
    }

    fun getOrderDishesQuantity (order: MutableState<Orders>): Int {
        return order.value.orderDishes.map { it.quantity }.sumOf { it.value }
    }

    fun getOrderTotalAmount (order: MutableState<Orders>): Double {
        return order.value.total
    }

    fun backToTables (navController: NavController) {
        navController.popBackStack()
    }

    fun getNotes(dishId: Int, order: MutableState<Orders>): String {
        return order.value.orderDishes
            .firstOrNull { it.dishId == dishId }
            ?.notes?.value
            ?: ""
    }

    fun isNoteEmpty (dishId: Int, order: MutableState<Orders>): Boolean {
        return getNotes(dishId, order).isEmpty()
    }

    fun updateNotes(dishId: Int, newNote: String, order: MutableState<Orders>) {
        order.value.orderDishes
            .firstOrNull { it.dishId == dishId }
            ?.notes?.value = newNote
    }
}