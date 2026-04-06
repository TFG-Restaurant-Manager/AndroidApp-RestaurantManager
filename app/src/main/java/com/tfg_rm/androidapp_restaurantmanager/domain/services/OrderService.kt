package com.tfg_rm.androidapp_restaurantmanager.domain.services

import com.tfg_rm.androidapp_restaurantmanager.data.repository.RepositoryOrders
import com.tfg_rm.androidapp_restaurantmanager.domain.models.Order
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Domain service that manages the business logic for customer orders.
 * * This service acts as a mediator for order-related operations, ensuring the presentation
 * layer receives data in a manageable format and providing mechanisms to refresh the state.
 *
 * @property repositoryOrders The repository responsible for consolidating and providing order data.
 */
@Singleton
class OrderService @Inject constructor(
    private val repositoryOrders: RepositoryOrders
) {
    /**
     * Retrieves the current list of active orders as a mutable collection.
     * * Converting the list to [MutableList] allows the UI layers to perform
     * local modifications if necessary before persisting changes.
     *
     * @return A mutable list of [Order] domain objects.
     */
    suspend fun getOrders(): MutableList<Order> = repositoryOrders.getOrders().toMutableList()

    /**
     * Invalidates the underlying data cache.
     * * This forces the system to fetch updated information from the server on the next request,
     * ensuring data consistency across the application.
     */
    fun clearCache() {
        repositoryOrders.clearCache()
    }
}
