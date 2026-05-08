package com.tfg_rm.androidapp_restaurantmanager.dao

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.tfg_rm.androidapp_restaurantmanager.data.local.dao.EmployeesDao
import com.tfg_rm.androidapp_restaurantmanager.data.local.database.AppDatabase
import com.tfg_rm.androidapp_restaurantmanager.data.local.entity.EmployeesEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented tests for [EmployeesDao].
 *
 * Covers CRUD operations and the reactive [Flow] from [EmployeesDao.getAllEmployees].
 */
@RunWith(AndroidJUnit4::class)
class EmployeesDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var dao: EmployeesDao

    private fun employee(id: Int, role: String = "Waiter") = EmployeesEntity(
        id = id, name = "Employee $id", roleName = role,
        email = "emp$id@test.com", numberPhone = "600000000", dni = "0000000${id}A"
    )

    @Before
    fun createDb() {
        val ctx = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(ctx, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = db.employeesDao()
    }

    @After
    fun closeDb() = db.close()

    // ── insertEmployee / getEmployeeById ──────────────────────────────────

    @Test
    fun insertEmployee_and_getById_returnsEmployee() = runBlocking {
        dao.insertEmployee(employee(1, role = "Chef"))

        val result = dao.getEmployeeById(1)

        assertNotNull(result)
        assertEquals(1, result!!.id)
        assertEquals("Chef", result.roleName)
    }

    @Test
    fun getEmployeeById_withMissingId_returnsNull() = runBlocking {
        assertNull(dao.getEmployeeById(999))
    }

    // ── insertEmployees / getAllEmployees ─────────────────────────────────

    @Test
    fun insertEmployees_and_getAll_returnsAll() = runBlocking {
        dao.insertEmployees(listOf(employee(1), employee(2), employee(3)))

        val all = dao.getAllEmployees().first()

        assertEquals(3, all.size)
    }

    // ── getAllEmployees as Flow emits on change ────────────────────────────

    @Test
    fun getAllEmployees_flow_emitsUpdatedListAfterInsert() = runBlocking {
        dao.insertEmployee(employee(1))
        assertEquals(1, dao.getAllEmployees().first().size)

        dao.insertEmployee(employee(2))
        assertEquals(2, dao.getAllEmployees().first().size)
    }

    // ── updateEmployee ────────────────────────────────────────────────────

    @Test
    fun updateEmployee_reflectsNewRole() = runBlocking {
        dao.insertEmployee(employee(1, role = "Waiter"))

        dao.updateEmployee(employee(1, role = "Manager"))

        assertEquals("Manager", dao.getEmployeeById(1)!!.roleName)
    }

    // ── deleteEmployee ────────────────────────────────────────────────────

    @Test
    fun deleteEmployee_removesFromDatabase() = runBlocking {
        dao.insertEmployee(employee(1))
        dao.deleteEmployee(employee(1))

        assertNull(dao.getEmployeeById(1))
    }

    // ── REPLACE conflict strategy ─────────────────────────────────────────

    @Test
    fun insertEmployee_withSameId_replacesExisting() = runBlocking {
        dao.insertEmployee(employee(1, role = "Waiter"))
        dao.insertEmployee(employee(1, role = "Chef")) // replace

        assertEquals("Chef", dao.getEmployeeById(1)!!.roleName)
    }
}
