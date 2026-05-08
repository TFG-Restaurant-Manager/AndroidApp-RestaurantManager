package com.tfg_rm.androidapp_restaurantmanager.di

import android.content.Context
import androidx.room.Room
import com.tfg_rm.androidapp_restaurantmanager.data.local.database.AppDatabase
import com.tfg_rm.androidapp_restaurantmanager.data.local.database.DatabaseModule
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

/**
 * Replaces [DatabaseModule] during instrumented tests with an in-memory Room database.
 * The in-memory database is discarded after each test process exits, ensuring test isolation.
 */
@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DatabaseModule::class]
)
object TestDatabaseModule {

    @Provides
    @Singleton
    fun provideInMemoryDatabase(
        @ApplicationContext context: Context
    ): AppDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
        .allowMainThreadQueries()
        .build()

    @Provides
    fun provideDishesDao(db: AppDatabase) = db.dishesDao()

    @Provides
    fun provideEmployeesDao(db: AppDatabase) = db.employeesDao()

    @Provides
    fun provideOrderItemsDao(db: AppDatabase) = db.orderItemsDao()

    @Provides
    fun provideOrdersDao(db: AppDatabase) = db.ordersDao()

    @Provides
    fun provideTablesDao(db: AppDatabase) = db.tablesDao()

    @Provides
    fun provideWorkSchedulesDao(db: AppDatabase) = db.workSchedulesDao()
}
