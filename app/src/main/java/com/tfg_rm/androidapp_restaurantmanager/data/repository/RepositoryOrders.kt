package com.tfg_rm.androidapp_restaurantmanager.data.repository

import androidx.compose.runtime.mutableIntStateOf
import com.tfg_rm.androidapp_restaurantmanager.data.remote.datasource.OrderRemoteDataSource
import com.tfg_rm.androidapp_restaurantmanager.data.remote.network.TokenProvider
import com.tfg_rm.androidapp_restaurantmanager.domain.models.Order
import com.tfg_rm.androidapp_restaurantmanager.domain.models.OrderItem
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepositoryOrders @Inject constructor(
    private val remote: OrderRemoteDataSource,
    private val dataDouble: TablesOrdersRepository,
    private val tokenProvider: TokenProvider
) {

    suspend fun getOrders(): List<Order> {
        return dataDouble.getTablesAndOrders()
            .filter { it.orderId != null }
            .groupBy { it.orderId }
            .map { (_, items) ->

                val first = items.first()

                Order(
                    id = first.orderId!!,
                    tableId = first.tableId,
                    status = first.orderStatus!!,
                    total = first.orderTotal!!,
                    notes = first.orderNotes,
                    createdAt = LocalDateTime.parse(first.orderCreatedAt),

                    orderItemsList = items
                        .groupBy { it.dishName }
                        .map { (_, dishes) ->
                            val firstDish = dishes.first()

                            OrderItem(
                                dishName = firstDish.dishName!!,
                                price = firstDish.dishPrice!!,
                                category = firstDish.categoryNam!!,
                                quantity = mutableIntStateOf(dishes.size)
                            )
                        }
                        .toMutableList()
                )
            }
    }

    fun clearCache() {
        dataDouble.clearCache()
    }
}
