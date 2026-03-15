package com.tfg_rm.androidapp_restaurantmanager.data.local.database

import com.tfg_rm.androidapp_restaurantmanager.data.local.entity.User
import androidx.room.Database
import androidx.room.RoomDatabase
import com.tfg_rm.androidapp_restaurantmanager.data.local.dao.UserDao

@Database(
    entities = [User::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}