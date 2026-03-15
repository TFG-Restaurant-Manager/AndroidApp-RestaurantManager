package com.tfg_rm.androidapp_restaurantmanager.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.tfg_rm.androidapp_restaurantmanager.data.local.entity.User

@Dao
interface UserDao {
    @Insert
    suspend fun insert(user: User)

    @Query("SELECT * FROM user_table")
    suspend fun getAllUsers(): List<User>
}