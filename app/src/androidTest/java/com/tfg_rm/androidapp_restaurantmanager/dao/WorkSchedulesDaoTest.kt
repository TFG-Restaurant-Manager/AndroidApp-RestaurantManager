package com.tfg_rm.androidapp_restaurantmanager.dao

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.tfg_rm.androidapp_restaurantmanager.data.local.dao.WorkSchedulesDao
import com.tfg_rm.androidapp_restaurantmanager.data.local.database.AppDatabase
import com.tfg_rm.androidapp_restaurantmanager.data.local.entity.WorkSchedulesEntity
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
 * Instrumented tests for [WorkSchedulesDao].
 *
 * Covers CRUD operations and the reactive [Flow] query.
 */
@RunWith(AndroidJUnit4::class)
class WorkSchedulesDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var dao: WorkSchedulesDao

    private fun schedule(id: Int, start: String = "08:00", end: String = "16:00") =
        WorkSchedulesEntity(id = id, start = start, end = end)

    @Before
    fun createDb() {
        val ctx = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(ctx, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = db.workSchedulesDao()
    }

    @After
    fun closeDb() = db.close()

    // ── insertWorkSchedule / getWorkScheduleById ──────────────────────────

    @Test
    fun insertSchedule_and_getById_returnsSchedule() = runBlocking {
        dao.insertWorkSchedule(schedule(1, start = "09:00", end = "17:00"))

        val result = dao.getWorkScheduleById(1)

        assertNotNull(result)
        assertEquals("09:00", result!!.start)
        assertEquals("17:00", result.end)
    }

    @Test
    fun getWorkScheduleById_withMissingId_returnsNull() = runBlocking {
        assertNull(dao.getWorkScheduleById(999))
    }

    // ── insertWorkSchedules / getAllWorkSchedules ──────────────────────────

    @Test
    fun insertSchedules_and_getAll_returnsAll() = runBlocking {
        dao.insertWorkSchedules(
            listOf(schedule(1), schedule(2, start = "12:00", end = "20:00"), schedule(3))
        )

        val all = dao.getAllWorkSchedules().first()
        assertEquals(3, all.size)
    }

    // ── getAllWorkSchedules as Flow ────────────────────────────────────────

    @Test
    fun getAllWorkSchedules_flow_emitsUpdatedListAfterInsert() = runBlocking {
        dao.insertWorkSchedule(schedule(1))
        assertEquals(1, dao.getAllWorkSchedules().first().size)

        dao.insertWorkSchedule(schedule(2))
        assertEquals(2, dao.getAllWorkSchedules().first().size)
    }

    // ── updateWorkSchedule ────────────────────────────────────────────────

    @Test
    fun updateSchedule_reflectsNewTimes() = runBlocking {
        dao.insertWorkSchedule(schedule(1, start = "08:00", end = "16:00"))

        dao.updateWorkSchedule(schedule(1, start = "10:00", end = "18:00"))

        val updated = dao.getWorkScheduleById(1)!!
        assertEquals("10:00", updated.start)
        assertEquals("18:00", updated.end)
    }

    // ── deleteWorkSchedule ────────────────────────────────────────────────

    @Test
    fun deleteSchedule_removesFromDatabase() = runBlocking {
        dao.insertWorkSchedule(schedule(1))
        dao.deleteWorkSchedule(schedule(1))

        assertNull(dao.getWorkScheduleById(1))
    }

    // ── REPLACE conflict strategy ─────────────────────────────────────────

    @Test
    fun insertSchedule_withSameId_replacesExisting() = runBlocking {
        dao.insertWorkSchedule(schedule(1, start = "08:00", end = "16:00"))
        dao.insertWorkSchedule(schedule(1, start = "11:00", end = "19:00")) // replace

        val result = dao.getWorkScheduleById(1)!!
        assertEquals("11:00", result.start)
    }
}
