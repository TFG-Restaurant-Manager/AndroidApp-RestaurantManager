package com.tfg_rm.androidapp_restaurantmanager.domain.services

import com.tfg_rm.androidapp_restaurantmanager.data.repository.RepositoryOrders
import com.tfg_rm.androidapp_restaurantmanager.domain.models.Order
import com.tfg_rm.androidapp_restaurantmanager.domain.models.Orders
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderService @Inject constructor(
    private val repositoryOrders: RepositoryOrders
) {
    fun savePendingOrder(order: Orders) = repositoryOrders.saveOrder(order)

    suspend fun getOrders(): MutableList<Order> = repositoryOrders.getOrders()
}
