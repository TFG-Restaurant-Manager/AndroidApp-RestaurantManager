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
import com.tfg_rm.androidapp_restaurantmanager.data.local.entity.OrderWithItemsEntity
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
 * Instrumented tests for [OrdersDao].
 *
 * Uses an in-memory Room database so no persistent data is left between runs.
 * Tests cover: CRUD operations and the @Transaction join queries
 * [OrdersDao.getOrdersWithItems] / [OrdersDao.getOrderWithItemsById].
 */
@RunWith(AndroidJUnit4::class)
class OrdersDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var ordersDao: OrdersDao
    private lateinit var dishesDao: DishesDao
    private lateinit var orderItemsDao: OrderItemsDao

    // ── Sample fixtures ───────────────────────────────────────────────────

    private fun order(id: Int, status: String = "CREATED") =
        OrdersEntity(id = id, tableId = 1, orderStatus = status, notes = "", createdAt = 0L)

    private fun dish(id: Int) =
        DishesEntity(id = id, name = "Dish $id", categoryName = "Cat", description = "", price = 5.0, available = true)

    private fun item(id: Int, orderId: Int, dishId: Int) =
        OrderItemsEntity(id = id, orderId = orderId, dishId = dishId, unitPrice = 5.0, notes = "")

    @Before
    fun createDb() {
        val ctx = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(ctx, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        ordersDao = db.ordersDao()
        dishesDao = db.dishesDao()
        orderItemsDao = db.orderItemsDao()
    }

    @After
    fun closeDb() = db.close()

    // ── insertOrder / getOrderById ────────────────────────────────────────

    @Test
    fun insertOrder_and_getById_returnsOrder() = runBlocking {
        ordersDao.insertOrder(order(1))

        val result = ordersDao.getOrderById(1)

        assertNotNull(result)
        assertEquals(1, result!!.id)
        assertEquals("CREATED", result.orderStatus)
    }

    @Test
    fun getOrderById_withMissingId_returnsNull() = runBlocking {
        val result = ordersDao.getOrderById(999)
        assertNull(result)
    }

    // ── insertOrders / getAllOrders ────────────────────────────────────────

    @Test
    fun insertOrders_and_getAll_returnsAll() = runBlocking {
        ordersDao.insertOrders(listOf(order(1), order(2), order(3)))

        val all = ordersDao.getAllOrders().first()

        assertEquals(3, all.size)
    }

    // ── updateOrder ───────────────────────────────────────────────────────

    @Test
    fun updateOrder_reflectsNewStatus() = runBlocking {
        ordersDao.insertOrder(order(1, "CREATED"))

        ordersDao.updateOrder(order(1, "COOKED"))

        assertEquals("COOKED", ordersDao.getOrderById(1)!!.orderStatus)
    }

    // ── deleteOrder ───────────────────────────────────────────────────────

    @Test
    fun deleteOrder_removesFromDatabase() = runBlocking {
        ordersDao.insertOrder(order(1))
        ordersDao.deleteOrder(order(1))

        assertNull(ordersDao.getOrderById(1))
    }

    // ── getOrdersWithItems ────────────────────────────────────────────────

    @Test
    fun getOrdersWithItems_returnsRelationWithItems() = runBlocking {
        dishesDao.insertDish(dish(10))
        ordersDao.insertOrder(order(1))
        orderItemsDao.insertOrderItem(item(id = 100, orderId = 1, dishId = 10))
        orderItemsDao.insertOrderItem(item(id = 101, orderId = 1, dishId = 10))

        val result: List<OrderWithItemsEntity> = ordersDao.getOrdersWithItems().first()

        assertEquals(1, result.size)
        assertEquals(2, result[0].items.size)
    }

    @Test
    fun getOrderWithItemsById_returnsCorrectRelation() = runBlocking {
        dishesDao.insertDish(dish(20))
        ordersDao.insertOrder(order(5))
        orderItemsDao.insertOrderItem(item(id = 200, orderId = 5, dishId = 20))

        val result: OrderWithItemsEntity? = ordersDao.getOrderWithItemsById(5).first()

        assertNotNull(result)
        assertEquals(5, result!!.order.id)
        assertEquals(1, result.items.size)
        assertEquals(200, result.items[0].id)
    }

    @Test
    fun getOrdersWithItems_withNoItems_returnsEmptyItemList() = runBlocking {
        ordersDao.insertOrder(order(2))

        val result = ordersDao.getOrdersWithItems().first()

        assertEquals(1, result.size)
        assertTrue(result[0].items.isEmpty())
    }

    // ── REPLACE conflict strategy ─────────────────────────────────────────

    @Test
    fun insertOrder_withSameId_replacesExisting() = runBlocking {
        ordersDao.insertOrder(order(1, "CREATED"))
        ordersDao.insertOrder(order(1, "PAID")) // replace

        assertEquals("PAID", ordersDao.getOrderById(1)!!.orderStatus)
    }
}
