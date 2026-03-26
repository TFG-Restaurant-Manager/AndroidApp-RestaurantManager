package com.tfg_rm.androidapp_restaurantmanager.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tfg_rm.androidapp_restaurantmanager.data.local.dao.DishesDao
import com.tfg_rm.androidapp_restaurantmanager.data.local.dao.EmployeesDao
import com.tfg_rm.androidapp_restaurantmanager.data.local.dao.OrderItemsDao
import com.tfg_rm.androidapp_restaurantmanager.data.local.dao.OrdersDao
import com.tfg_rm.androidapp_restaurantmanager.data.local.dao.TablesDao
import com.tfg_rm.androidapp_restaurantmanager.data.local.dao.WorkSchedulesDao
import com.tfg_rm.androidapp_restaurantmanager.data.local.entity.DishesEntity
import com.tfg_rm.androidapp_restaurantmanager.data.local.entity.EmployeesEntity
import com.tfg_rm.androidapp_restaurantmanager.data.local.entity.OrderItemsEntity
import com.tfg_rm.androidapp_restaurantmanager.data.local.entity.OrdersEntity
import com.tfg_rm.androidapp_restaurantmanager.data.local.entity.TablesEntity
import com.tfg_rm.androidapp_restaurantmanager.data.local.entity.WorkSchedulesEntity

@Database(
    entities = [
        DishesEntity::class,
        EmployeesEntity::class,
        OrderItemsEntity::class,
        OrdersEntity::class,
        TablesEntity::class,
        WorkSchedulesEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dishesDao(): DishesDao
    abstract fun employeesDao(): EmployeesDao
    abstract fun orderItemsDao(): OrderItemsDao
    abstract fun ordersDao(): OrdersDao
    abstract fun tablesDao(): TablesDao
    abstract fun workSchedulesDao(): WorkSchedulesDao
}
