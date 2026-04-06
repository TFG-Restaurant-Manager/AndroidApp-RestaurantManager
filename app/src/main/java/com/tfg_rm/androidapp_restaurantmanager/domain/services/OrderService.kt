package com.tfg_rm.androidapp_restaurantmanager.domain.services

import com.tfg_rm.androidapp_restaurantmanager.data.repository.RepositoryOrders
import com.tfg_rm.androidapp_restaurantmanager.domain.models.Order
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderService @Inject constructor(
    private val repository: RepositoryOrders
) {
    suspend fun getOrders(): MutableList<Order> = repository.getOrders().toMutableList()

    fun clearCache() {
        repository.clearCache()
    }

    val events = repository.events

    suspend fun sendUpdate(message: String) {
        repository.sendUpdate(message)
    }
}
