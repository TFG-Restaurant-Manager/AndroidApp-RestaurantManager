package com.tfg_rm.androidapp_restaurantmanager.dao

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.tfg_rm.androidapp_restaurantmanager.data.local.dao.DishesDao
import com.tfg_rm.androidapp_restaurantmanager.data.local.database.AppDatabase
import com.tfg_rm.androidapp_restaurantmanager.data.local.entity.DishesEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented tests for [DishesDao].
 *
 * Covers CRUD operations and Flow-based queries.
 */
@RunWith(AndroidJUnit4::class)
class DishesDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var dao: DishesDao

    private fun dish(
        id: Int,
        name: String = "Dish $id",
        category: String = "Mains",
        available: Boolean = true,
        price: Double = 10.0
    ) = DishesEntity(
        id = id, name = name, categoryName = category,
        description = "Description $id", price = price, available = available
    )

    @Before
    fun createDb() {
        val ctx = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(ctx, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = db.dishesDao()
    }

    @After
    fun closeDb() = db.close()

    // ── insertDish / getDishById ──────────────────────────────────────────

    @Test
    fun insertDish_and_getById_returnsDish() = runBlocking {
        dao.insertDish(dish(1, name = "Pizza"))

        val result = dao.getDishById(1)

        assertNotNull(result)
        assertEquals("Pizza", result!!.name)
        assertEquals(1, result.id)
    }

    @Test
    fun getDishById_withMissingId_returnsNull() = runBlocking {
        val result = dao.getDishById(999)
        assertNull(result)
    }

    // ── insertDishes / getAllDishes ────────────────────────────────────────

    @Test
    fun insertDishes_and_getAll_returnsAll() = runBlocking {
        dao.insertDishes(listOf(dish(1), dish(2), dish(3)))

        val all = dao.getAllDishes().first()

        assertEquals(3, all.size)
    }

    // ── getAllDishes as Flow emits on change ──────────────────────────────

    @Test
    fun getAllDishes_flow_emitsUpdatedListAfterInsert() = runBlocking {
        dao.insertDish(dish(1))
        assertEquals(1, dao.getAllDishes().first().size)

        dao.insertDish(dish(2))
        assertEquals(2, dao.getAllDishes().first().size)
    }

    // ── updateDish ────────────────────────────────────────────────────────

    @Test
    fun updateDish_reflectsNewAvailability() = runBlocking {
        dao.insertDish(dish(1, available = true))

        dao.updateDish(dish(1, available = false))

        assertFalse(dao.getDishById(1)!!.available)
    }

    @Test
    fun updateDish_reflectsNewPrice() = runBlocking {
        dao.insertDish(dish(1, price = 10.0))

        dao.updateDish(dish(1, price = 15.50))

        assertEquals(15.50, dao.getDishById(1)!!.price, 0.001)
    }

    // ── deleteDish ────────────────────────────────────────────────────────

    @Test
    fun deleteDish_removesFromDatabase() = runBlocking {
        dao.insertDish(dish(1))
        dao.deleteDish(dish(1))

        assertNull(dao.getDishById(1))
    }

    // ── REPLACE conflict strategy ─────────────────────────────────────────

    @Test
    fun insertDish_withSameId_replacesExisting() = runBlocking {
        dao.insertDish(dish(1, name = "Old Burger"))
        dao.insertDish(dish(1, name = "New Burger")) // replace

        assertEquals("New Burger", dao.getDishById(1)!!.name)
    }

    // ── available flag ────────────────────────────────────────────────────

    @Test
    fun insertDish_withAvailableTrue_persistsFlag() = runBlocking {
        dao.insertDish(dish(1, available = true))
        assertTrue(dao.getDishById(1)!!.available)
    }

    @Test
    fun insertDish_withAvailableFalse_persistsFlag() = runBlocking {
        dao.insertDish(dish(1, available = false))
        assertFalse(dao.getDishById(1)!!.available)
    }
}
