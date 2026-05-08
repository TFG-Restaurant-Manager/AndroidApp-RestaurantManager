package com.tfg_rm.androidapp_restaurantmanager.dao

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.tfg_rm.androidapp_restaurantmanager.data.local.dao.DishesDao
import com.tfg_rm.androidapp_restaurantmanager.data.local.dao.OrderItemsDao
import com.tfg_rm.androidapp_restaurantmanager.data.local.dao.OrdersDao
import com.tfg_rm.androidapp_restaurantmanager.data.local.database.AppDatabase
import com.tfg_rm.androidapp_restaurantmanager.data.local.entity.DishesEntity
import com.tfg_rm.androidapp_restaurantmanager.data.local.entity.OrderItemsEntity
import com.tfg_rm.androidapp_restaurantmanager.data.local.entity.OrdersEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented tests for [OrderItemsDao].
 *
 * [OrderItemsEntity] has foreign-key constraints on [OrdersEntity] (CASCADE delete)
 * and [DishesEntity], so parent rows are inserted in [setUp] before each test.
 *
 * Covers:
 * - CRUD operations
 * - [OrderItemsDao.getOrderItemsByOrderId] scoped query
 * - [OrderItemsDao.deleteOrderItemsByOrderId] bulk delete
 * - CASCADE delete when parent order is removed
 */
@RunWith(AndroidJUnit4::class)
class OrderItemsDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var orderItemsDao: OrderItemsDao
    private lateinit var ordersDao: OrdersDao
    private lateinit var dishesDao: DishesDao

    // ── Fixtures ──────────────────────────────────────────────────────────

    private fun order(id: Int) =
        OrdersEntity(id = id, tableId = 1, orderStatus = "CREATED", notes = "", createdAt = 0L)

    private fun dish(id: Int) =
        DishesEntity(id = id, name = "Dish $id", categoryName = "Cat", description = "", price = 5.0, available = true)

    private fun item(id: Int, orderId: Int, dishId: Int, notes: String = "") =
        OrderItemsEntity(id = id, orderId = orderId, dishId = dishId, unitPrice = 5.0, notes = notes)

    @Before
    fun createDb() = runBlocking {
        val ctx = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(ctx, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        orderItemsDao = db.orderItemsDao()
        ordersDao = db.ordersDao()
        dishesDao = db.dishesDao()

        // Insert parent entities required by foreign keys
        dishesDao.insertDish(dish(1))
        dishesDao.insertDish(dish(2))
        ordersDao.insertOrder(order(10))
        ordersDao.insertOrder(order(20))
    }

    @After
    fun closeDb() = db.close()

    // ── insertOrderItem / getAllOrderItems ────────────────────────────────

    @Test
    fun insertOrderItem_and_getAll_returnsItem() = runBlocking {
        orderItemsDao.insertOrderItem(item(id = 1, orderId = 10, dishId = 1))

        val all = orderItemsDao.getAllOrderItems().first()

        assertEquals(1, all.size)
        assertEquals(1, all[0].id)
    }

    @Test
    fun insertOrderItems_and_getAll_returnsAll() = runBlocking {
        orderItemsDao.insertOrderItems(
            listOf(
                item(id = 1, orderId = 10, dishId = 1),
                item(id = 2, orderId = 10, dishId = 2),
                item(id = 3, orderId = 20, dishId = 1)
            )
        )

        val all = orderItemsDao.getAllOrderItems().first()
        assertEquals(3, all.size)
    }

    // ── getOrderItemsByOrderId ────────────────────────────────────────────

    @Test
    fun getOrderItemsByOrderId_returnsOnlyItemsForThatOrder() = runBlocking {
        orderItemsDao.insertOrderItems(
            listOf(
                item(id = 1, orderId = 10, dishId = 1),
                item(id = 2, orderId = 10, dishId = 2),
                item(id = 3, orderId = 20, dishId = 1) // different order
            )
        )

        val itemsForOrder10 = orderItemsDao.getOrderItemsByOrderId(10).first()

        assertEquals(2, itemsForOrder10.size)
        assertTrue(itemsForOrder10.all { it.orderId == 10 })
    }

    @Test
    fun getOrderItemsByOrderId_withNoItems_returnsEmptyList() = runBlocking {
        val result = orderItemsDao.getOrderItemsByOrderId(10).first()
        assertTrue(result.isEmpty())
    }

    // ── updateOrderItem ───────────────────────────────────────────────────

    @Test
    fun updateOrderItem_reflectsNewNotes() = runBlocking {
        orderItemsDao.insertOrderItem(item(id = 1, orderId = 10, dishId = 1, notes = ""))

        orderItemsDao.updateOrderItem(item(id = 1, orderId = 10, dishId = 1, notes = "No onions"))

        val updated = orderItemsDao.getAllOrderItems().first().first { it.id == 1 }
        assertEquals("No onions", updated.notes)
    }

    // ── deleteOrderItem ───────────────────────────────────────────────────

    @Test
    fun deleteOrderItem_removesFromDatabase() = runBlocking {
        orderItemsDao.insertOrderItem(item(id = 1, orderId = 10, dishId = 1))

        orderItemsDao.deleteOrderItem(item(id = 1, orderId = 10, dishId = 1))

        val all = orderItemsDao.getAllOrderItems().first()
        assertTrue(all.isEmpty())
    }

    // ── deleteOrderItemsByOrderId ─────────────────────────────────────────

    @Test
    fun deleteOrderItemsByOrderId_removesAllItemsForThatOrder() = runBlocking {
        orderItemsDao.insertOrderItems(
            listOf(
                item(id = 1, orderId = 10, dishId = 1),
                item(id = 2, orderId = 10, dishId = 2),
                item(id = 3, orderId = 20, dishId = 1) // different order — should survive
            )
        )

        orderItemsDao.deleteOrderItemsByOrderId(10)

        val remaining = orderItemsDao.getAllOrderItems().first()
        assertEquals(1, remaining.size)
        assertEquals(20, remaining[0].orderId)
    }

    // ── CASCADE delete ────────────────────────────────────────────────────

    @Test
    fun deleteParentOrder_cascadeDeletesItsItems() = runBlocking {
        orderItemsDao.insertOrderItem(item(id = 1, orderId = 10, dishId = 1))
        orderItemsDao.insertOrderItem(item(id = 2, orderId = 10, dishId = 2))

        ordersDao.deleteOrder(order(10)) // cascade

        val remaining = orderItemsDao.getAllOrderItems().first()
        assertTrue(remaining.isEmpty())
    }

    // ── REPLACE conflict strategy ─────────────────────────────────────────

    @Test
    fun insertOrderItem_withSameId_replacesExisting() = runBlocking {
        orderItemsDao.insertOrderItem(item(id = 1, orderId = 10, dishId = 1, notes = "Original"))
        orderItemsDao.insertOrderItem(item(id = 1, orderId = 10, dishId = 1, notes = "Replaced"))

        val all = orderItemsDao.getAllOrderItems().first()
        assertNotNull(all.find { it.notes == "Replaced" })
        assertNull(all.find { it.notes == "Original" })
    }
}
