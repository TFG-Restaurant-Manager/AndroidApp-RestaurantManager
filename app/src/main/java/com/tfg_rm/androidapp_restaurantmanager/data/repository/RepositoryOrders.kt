package com.tfg_rm.androidapp_restaurantmanager.data.repository

import com.tfg_rm.androidapp_restaurantmanager.data.remote.datasource.OrderRemoteDataSource
import com.tfg_rm.androidapp_restaurantmanager.data.remote.network.TokenProvider
import com.tfg_rm.androidapp_restaurantmanager.domain.models.Order
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
                    orderItemsList = first.orderItems!!.toMutableList()
                )
            }
    }

    fun clearCache() {
        dataDouble.clearCache()
    }
}
