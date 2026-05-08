package com.tfg_rm.androidapp_restaurantmanager.viewmodels

import androidx.compose.runtime.mutableStateOf
import app.cash.turbine.test
import com.tfg_rm.androidapp_restaurantmanager.R
import com.tfg_rm.androidapp_restaurantmanager.domain.models.Dishes
import com.tfg_rm.androidapp_restaurantmanager.domain.models.Order
import com.tfg_rm.androidapp_restaurantmanager.domain.models.UiState
import com.tfg_rm.androidapp_restaurantmanager.domain.services.FoodService
import com.tfg_rm.androidapp_restaurantmanager.domain.services.OrderService
import com.tfg_rm.androidapp_restaurantmanager.domain.viewmodels.FoodViewModel
import com.tfg_rm.androidapp_restaurantmanager.utils.MainCoroutineRule
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Unit tests for [FoodViewModel].
 *
 * Covers:
 * - getDishes() → Loading → Success / Error transitions
 * - getDishesCategories() prepends "Todo" before unique categories
 * - filterDishes() by category, search string (case-insensitive), and both combined
 * - addDishToOrder() / minusDishOnOrder() update the order state and total
 * - isDishInOrder() / getDishQuantityInOrder() helpers
 */
@OptIn(ExperimentalCoroutinesApi::class)
class FoodViewModelTest {

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    private lateinit var foodService: FoodService
    private lateinit var orderService: OrderService
    private lateinit var viewModel: FoodViewModel

    private fun dish(
        id: Int,
        name: String = "Dish $id",
        category: String = "Category",
        price: Double = 10.0
    ) = Dishes(id = id, name = name, description = "desc", category = category, price = price, available = true)

    private fun emptyOrder() = Order(
        id = 0, tableId = 1, tableName = "T1", type = null,
        status = "CREATED", total = 0.0
    )

    @Before
    fun setUp() {
        foodService = mockk(relaxed = true)
        orderService = mockk(relaxed = true)
        every { orderService.observeMessages() } returns emptyFlow()
        viewModel = FoodViewModel(foodService, orderService)
    }

    // ── getDishes ─────────────────────────────────────────────────────────

    @Test
    fun `getDishes emits Loading then Success`() = runTest {
        val dishList = listOf(dish(1), dish(2))
        coEvery { foodService.getDishes() } returns dishList

        viewModel.dishes.test {
            awaitItem() // initial Idle

            viewModel.getDishes()

            val loading = awaitItem()
            assertEquals(UiState.Loading, loading)

            val success = awaitItem()
            assertTrue(success is UiState.Success)
            assertEquals(2, (success as UiState.Success).data.size)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getDishes emits Error on exception`() = runTest {
        coEvery { foodService.getDishes() } throws Exception("timeout")

        viewModel.dishes.test {
            awaitItem() // initial Idle
            viewModel.getDishes()
            awaitItem() // Loading

            val error = awaitItem()
            assertTrue(error is UiState.Error)
            assertEquals(R.string.foodscreen_error, (error as UiState.Error).message)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `resetState returns to Idle`() = runTest {
        coEvery { foodService.getDishes() } returns emptyList()
        viewModel.getDishes()

        viewModel.resetState()

        assertEquals(UiState.Idle, viewModel.dishes.value)
    }

    // ── getDishesCategories ───────────────────────────────────────────────

    @Test
    fun `getDishesCategories prepends Todo and returns distinct categories`() {
        val dishes = listOf(
            dish(1, category = "Starters"),
            dish(2, category = "Mains"),
            dish(3, category = "Starters"), // duplicate
            dish(4, category = "Drinks")
        )

        val categories = viewModel.getDishesCategories(dishes)

        assertEquals(listOf("Todo", "Starters", "Mains", "Drinks"), categories)
    }

    @Test
    fun `getDishesCategories with empty list returns only Todo`() {
        val categories = viewModel.getDishesCategories(emptyList())
        assertEquals(listOf("Todo"), categories)
    }

    // ── filterDishes ──────────────────────────────────────────────────────

    @Test
    fun `filterDishes with Todo category returns all`() {
        val dishes = listOf(dish(1, category = "Mains"), dish(2, category = "Drinks"))

        val result = viewModel.filterDishes(dishes, searchedDish = "", selectedCategory = "Todo")

        assertEquals(2, result.size)
    }

    @Test
    fun `filterDishes by specific category returns only matching`() {
        val dishes = listOf(
            dish(1, category = "Mains"),
            dish(2, category = "Drinks"),
            dish(3, category = "Mains")
        )

        val result = viewModel.filterDishes(dishes, "", "Mains")

        assertEquals(2, result.size)
        assertTrue(result.all { it.category == "Mains" })
    }

    @Test
    fun `filterDishes by search is case-insensitive`() {
        val dishes = listOf(
            dish(1, name = "Beef Burger", category = "Mains"),
            dish(2, name = "Chicken Wings", category = "Starters"),
            dish(3, name = "BEEF STEAK", category = "Mains")
        )

        val result = viewModel.filterDishes(dishes, searchedDish = "beef", selectedCategory = "Todo")

        assertEquals(2, result.size)
    }

    @Test
    fun `filterDishes combined category and search narrows results`() {
        val dishes = listOf(
            dish(1, name = "Beef Burger", category = "Mains"),
            dish(2, name = "Beef Tacos", category = "Starters"),
            dish(3, name = "Chicken Burger", category = "Mains")
        )

        val result = viewModel.filterDishes(dishes, "Burger", "Mains")

        assertEquals(1, result.size)
        assertEquals("Beef Burger", result[0].name)
    }

    @Test
    fun `filterDishes with blank search returns entire category`() {
        val dishes = listOf(dish(1, category = "Mains"), dish(2, category = "Mains"))

        val result = viewModel.filterDishes(dishes, "  ", "Mains")

        assertEquals(2, result.size)
    }

    // ── addDishToOrder / minusDishOnOrder ─────────────────────────────────

    @Test
    fun `addDishToOrder adds item and updates total`() {
        val order = mutableStateOf(emptyOrder())
        val pizza = dish(1, name = "Pizza", price = 12.0)

        viewModel.addDishToOrder(order, pizza)

        assertEquals(1, order.value.orderItemsList.size)
        assertEquals(1, order.value.orderItemsList[0].dishId)
        assertEquals(12.0, order.value.total, 0.001)
    }

    @Test
    fun `addDishToOrder twice adds two items with accumulated total`() {
        val order = mutableStateOf(emptyOrder())
        val burger = dish(2, name = "Burger", price = 8.0)

        viewModel.addDishToOrder(order, burger)
        viewModel.addDishToOrder(order, burger)

        assertEquals(2, order.value.orderItemsList.size)
        assertEquals(16.0, order.value.total, 0.001)
    }

    @Test
    fun `minusDishOnOrder removes one item and updates total`() {
        val order = mutableStateOf(emptyOrder())
        val salad = dish(3, name = "Salad", price = 7.0)

        viewModel.addDishToOrder(order, salad)
        viewModel.addDishToOrder(order, salad)

        viewModel.minusDishOnOrder(order, salad)

        assertEquals(1, order.value.orderItemsList.size)
        assertEquals(7.0, order.value.total, 0.001)
    }

    @Test
    fun `minusDishOnOrder on absent dish does not change order`() {
        val order = mutableStateOf(emptyOrder())
        val salad = dish(3, name = "Salad", price = 7.0)

        viewModel.minusDishOnOrder(order, salad) // nothing to remove

        assertTrue(order.value.orderItemsList.isEmpty())
        assertEquals(0.0, order.value.total, 0.001)
    }

    // ── isDishInOrder / getDishQuantityInOrder ────────────────────────────

    @Test
    fun `isDishInOrder returns true when dish is present`() {
        val order = mutableStateOf(emptyOrder())
        val pasta = dish(4, name = "Pasta", price = 9.0)
        viewModel.addDishToOrder(order, pasta)

        assertTrue(viewModel.isDishInOrder(order, pasta))
    }

    @Test
    fun `isDishInOrder returns false when dish is absent`() {
        val order = mutableStateOf(emptyOrder())
        val pasta = dish(4, name = "Pasta", price = 9.0)

        assertFalse(viewModel.isDishInOrder(order, pasta))
    }

    @Test
    fun `getDishQuantityInOrder returns correct count`() {
        val order = mutableStateOf(emptyOrder())
        val sushi = dish(5, name = "Sushi", price = 15.0)

        viewModel.addDishToOrder(order, sushi)
        viewModel.addDishToOrder(order, sushi)
        viewModel.addDishToOrder(order, sushi)

        assertEquals(3, viewModel.getDishQuantityInOrder(order, sushi))
    }
}
