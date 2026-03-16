package com.tfg_rm.androidapp_restaurantmanager.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.tfg_rm.androidapp_restaurantmanager.data.local.entity.TablesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TablesDao {
    @Query("SELECT * FROM tables")
    fun getAllTables(): Flow<List<TablesEntity>>

    @Query("SELECT * FROM tables WHERE id = :id")
    suspend fun getTableById(id: Int): TablesEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTable(table: TablesEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTables(tables: List<TablesEntity>)

    @Update
    suspend fun updateTable(table: TablesEntity)

    @Delete
    suspend fun deleteTable(table: TablesEntity)
}
