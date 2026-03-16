package com.tfg_rm.androidapp_restaurantmanager.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.tfg_rm.androidapp_restaurantmanager.data.local.entity.OrderWithItemsEntity
import com.tfg_rm.androidapp_restaurantmanager.data.local.entity.OrdersEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OrdersDao {
    @Query("SELECT * FROM orders")
    fun getAllOrders(): Flow<List<OrdersEntity>>

    @Query("SELECT * FROM orders WHERE id = :id")
    suspend fun getOrderById(id: Int): OrdersEntity?

    @Transaction
    @Query("SELECT * FROM orders")
    fun getOrdersWithItems(): Flow<List<OrderWithItemsEntity>>

    @Transaction
    @Query("SELECT * FROM orders WHERE id = :orderId")
    fun getOrderWithItemsById(orderId: Int): Flow<OrderWithItemsEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: OrdersEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrders(orders: List<OrdersEntity>)

    @Update
    suspend fun updateOrder(order: OrdersEntity)

    @Delete
    suspend fun deleteOrder(order: OrdersEntity)
}
