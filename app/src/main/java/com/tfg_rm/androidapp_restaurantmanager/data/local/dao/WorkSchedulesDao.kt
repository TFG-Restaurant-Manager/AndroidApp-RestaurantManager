package com.tfg_rm.androidapp_restaurantmanager.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.tfg_rm.androidapp_restaurantmanager.data.local.entity.WorkSchedulesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkSchedulesDao {
    @Query("SELECT * FROM work_schedules")
    fun getAllWorkSchedules(): Flow<List<WorkSchedulesEntity>>

    @Query("SELECT * FROM work_schedules WHERE id = :id")
    suspend fun getWorkScheduleById(id: Int): WorkSchedulesEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkSchedule(workSchedule: WorkSchedulesEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkSchedules(workSchedules: List<WorkSchedulesEntity>)

    @Update
    suspend fun updateWorkSchedule(workSchedule: WorkSchedulesEntity)

    @Delete
    suspend fun deleteWorkSchedule(workSchedule: WorkSchedulesEntity)
}
