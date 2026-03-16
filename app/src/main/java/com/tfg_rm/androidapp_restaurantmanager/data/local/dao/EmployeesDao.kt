package com.tfg_rm.androidapp_restaurantmanager.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.tfg_rm.androidapp_restaurantmanager.data.local.entity.EmployeesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EmployeesDao {
    @Query("SELECT * FROM employees")
    fun getAllEmployees(): Flow<List<EmployeesEntity>>

    @Query("SELECT * FROM employees WHERE id = :id")
    suspend fun getEmployeeById(id: Int): EmployeesEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmployee(employee: EmployeesEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmployees(employees: List<EmployeesEntity>)

    @Update
    suspend fun updateEmployee(employee: EmployeesEntity)

    @Delete
    suspend fun deleteEmployee(employee: EmployeesEntity)
}
