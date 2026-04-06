package com.tfg_rm.androidapp_restaurantmanager.domain.services

import com.tfg_rm.androidapp_restaurantmanager.data.repository.RepositoryFood
import com.tfg_rm.androidapp_restaurantmanager.domain.models.Dishes
import com.tfg_rm.androidapp_restaurantmanager.domain.models.Order
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Domain service that manages business logic related to the restaurant's menu and food orders.
 * * This service acts as a specialized entry point for the presentation layer to interact
 * with food data, abstracting the operations provided by [RepositoryFood].
 *
 * @property repositoryFood The repository responsible for food data persistence and retrieval.
 */
@Singleton
class FoodService @Inject constructor(
    private val repositoryFood: RepositoryFood
) {
    /**
     * Retrieves the available menu items categorized as dishes.
     *
     * @return A list of [Dishes] representing the current menu offerings.
     */
    suspend fun getDishes(): List<Dishes> = repositoryFood.getDishes()

    /**
     * Processes and stores a new or updated customer order.
     *
     * @param order The [Order] domain object to be saved in the system.
     */
    fun saveOrder(order: Order) {
        repositoryFood.saveOrder(order)
    }
}
