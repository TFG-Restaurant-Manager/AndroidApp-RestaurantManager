package com.tfg_rm.androidapp_restaurantmanager.dao

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.tfg_rm.androidapp_restaurantmanager.data.local.dao.TablesDao
import com.tfg_rm.androidapp_restaurantmanager.data.local.database.AppDatabase
import com.tfg_rm.androidapp_restaurantmanager.data.local.entity.TablesEntity
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
 * Instrumented tests for [TablesDao].
 *
 * Covers CRUD operations and reactive [Flow] updates via [TablesDao.getAllTables].
 */
@RunWith(AndroidJUnit4::class)
class TablesDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var dao: TablesDao

    private fun table(id: Int, section: String = "Main", status: String = "AVAILABLE") =
        TablesEntity(id = id, sectionName = section, capacity = 4, posX = 0, posY = 0, status = status)

    @Before
    fun createDb() {
        val ctx = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(ctx, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = db.tablesDao()
    }

    @After
    fun closeDb() = db.close()

    // ── insertTable / getTableById ────────────────────────────────────────

    @Test
    fun insertTable_and_getById_returnsTable() = runBlocking {
        dao.insertTable(table(1))

        val result = dao.getTableById(1)

        assertNotNull(result)
        assertEquals(1, result!!.id)
        assertEquals("Main", result.sectionName)
    }

    @Test
    fun getTableById_withMissingId_returnsNull() = runBlocking {
        val result = dao.getTableById(999)
        assertNull(result)
    }

    // ── insertTables / getAllTables ────────────────────────────────────────

    @Test
    fun insertTables_and_getAll_returnsAll() = runBlocking {
        dao.insertTables(listOf(table(1), table(2), table(3, section = "Terrace")))

        val all = dao.getAllTables().first()

        assertEquals(3, all.size)
    }

    // ── getAllTables as Flow emits on insert ──────────────────────────────

    @Test
    fun getAllTables_flow_emitsUpdatedListAfterInsert() = runBlocking {
        dao.insertTable(table(1))
        val firstEmit = dao.getAllTables().first()
        assertEquals(1, firstEmit.size)

        dao.insertTable(table(2))
        val secondEmit = dao.getAllTables().first()
        assertEquals(2, secondEmit.size)
    }

    // ── updateTable ───────────────────────────────────────────────────────

    @Test
    fun updateTable_reflectsNewStatus() = runBlocking {
        dao.insertTable(table(1, status = "AVAILABLE"))

        dao.updateTable(table(1, status = "OCCUPIED"))

        assertEquals("OCCUPIED", dao.getTableById(1)!!.status)
    }

    // ── deleteTable ───────────────────────────────────────────────────────

    @Test
    fun deleteTable_removesFromDatabase() = runBlocking {
        dao.insertTable(table(1))
        dao.deleteTable(table(1))

        assertNull(dao.getTableById(1))
    }

    // ── REPLACE conflict strategy ─────────────────────────────────────────

    @Test
    fun insertTable_withSameId_replacesExisting() = runBlocking {
        dao.insertTable(table(1, section = "Main"))
        dao.insertTable(table(1, section = "Terrace")) // replace

        assertEquals("Terrace", dao.getTableById(1)!!.sectionName)
    }
}
