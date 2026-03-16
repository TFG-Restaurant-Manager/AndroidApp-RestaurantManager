package com.tfg_rm.androidapp_restaurantmanager.data.local.database

import android.content.Context
import androidx.room.Room
import com.tfg_rm.androidapp_restaurantmanager.data.local.dao.DishesDao
import com.tfg_rm.androidapp_restaurantmanager.data.local.dao.EmployeesDao
import com.tfg_rm.androidapp_restaurantmanager.data.local.dao.OrderItemsDao
import com.tfg_rm.androidapp_restaurantmanager.data.local.dao.OrdersDao
import com.tfg_rm.androidapp_restaurantmanager.data.local.dao.TablesDao
import com.tfg_rm.androidapp_restaurantmanager.data.local.dao.WorkSchedulesDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "restaurant_manager"
        ).build()
    }

    @Provides
    fun provideDishesDao(db: AppDatabase): DishesDao = db.dishesDao()

    @Provides
    fun provideEmployeesDao(db: AppDatabase): EmployeesDao = db.employeesDao()

    @Provides
    fun provideOrderItemsDao(db: AppDatabase): OrderItemsDao = db.orderItemsDao()

    @Provides
    fun provideOrdersDao(db: AppDatabase): OrdersDao = db.ordersDao()

    @Provides
    fun provideTablesDao(db: AppDatabase): TablesDao = db.tablesDao()

    @Provides
    fun provideWorkSchedulesDao(db: AppDatabase): WorkSchedulesDao = db.workSchedulesDao()
}
