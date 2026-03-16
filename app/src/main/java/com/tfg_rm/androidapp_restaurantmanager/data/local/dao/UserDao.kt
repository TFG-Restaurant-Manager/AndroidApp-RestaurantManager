package com.tfg_rm.androidapp_restaurantmanager.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.tfg_rm.androidapp_restaurantmanager.data.local.entity.Tables

@Dao
interface UserDao {
    @Insert
    suspend fun insert(user: Tables)

    @Query("SELECT * FROM tables")
    suspend fun getAllUsers(): List<Tables>
}