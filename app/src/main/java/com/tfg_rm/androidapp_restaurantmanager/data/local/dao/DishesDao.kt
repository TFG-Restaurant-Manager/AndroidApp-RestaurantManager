package com.tfg_rm.androidapp_restaurantmanager.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.tfg_rm.androidapp_restaurantmanager.data.local.entity.DishesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DishesDao {
    @Query("SELECT * FROM dishes")
    fun getAllDishes(): Flow<List<DishesEntity>>

    @Query("SELECT * FROM dishes WHERE id = :id")
    suspend fun getDishById(id: Int): DishesEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDish(dish: DishesEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDishes(dishes: List<DishesEntity>)

    @Update
    suspend fun updateDish(dish: DishesEntity)

    @Delete
    suspend fun deleteDish(dish: DishesEntity)
}
