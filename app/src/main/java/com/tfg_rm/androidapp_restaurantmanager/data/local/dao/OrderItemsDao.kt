package com.tfg_rm.androidapp_restaurantmanager.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.tfg_rm.androidapp_restaurantmanager.data.local.entity.OrderItemsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderItemsDao {
    @Query("SELECT * FROM order_items")
    fun getAllOrderItems(): Flow<List<OrderItemsEntity>>

    @Query("SELECT * FROM order_items WHERE orderId = :orderId")
    fun getOrderItemsByOrderId(orderId: Int): Flow<List<OrderItemsEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrderItem(orderItem: OrderItemsEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrderItems(orderItems: List<OrderItemsEntity>)

    @Update
    suspend fun updateOrderItem(orderItem: OrderItemsEntity)

    @Delete
    suspend fun deleteOrderItem(orderItem: OrderItemsEntity)

    @Query("DELETE FROM order_items WHERE orderId = :orderId")
    suspend fun deleteOrderItemsByOrderId(orderId: Int)
}
