package com.tfg_rm.androidapp_restaurantmanager.data.repository

import com.tfg_rm.androidapp_restaurantmanager.data.remote.datasource.OrderRemoteDataSource
import com.tfg_rm.androidapp_restaurantmanager.data.remote.mapper.toOrder
import com.tfg_rm.androidapp_restaurantmanager.data.remote.network.TokenProvider
import com.tfg_rm.androidapp_restaurantmanager.domain.models.Order
import com.tfg_rm.androidapp_restaurantmanager.domain.models.Orders
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepositoryOrders @Inject constructor(
    private val remote: OrderRemoteDataSource,
    private val tokenProvider: TokenProvider
) {

    suspend fun getOrders(): List<Order> {
        return remote.getOrders().map { it.toOrder() }
    }

    // In-memory pending Orders per table (used by Food flow)


    fun saveOrder(order: Orders) {
        //dddd
    }
}
