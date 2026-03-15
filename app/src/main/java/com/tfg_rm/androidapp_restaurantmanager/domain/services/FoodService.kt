package com.tfg_rm.androidapp_restaurantmanager.domain.services

import com.tfg_rm.androidapp_restaurantmanager.data.repository.RepositoryFood
import com.tfg_rm.androidapp_restaurantmanager.domain.services.OrderService
import com.tfg_rm.androidapp_restaurantmanager.domain.models.Dishes
import com.tfg_rm.androidapp_restaurantmanager.domain.models.Order
import com.tfg_rm.androidapp_restaurantmanager.domain.models.Orders
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FoodService @Inject constructor(
    private val repositoryFood: RepositoryFood,
    private val orderService: OrderService
) {
    suspend fun getDishes(): List<Dishes> = repositoryFood.getDishes()

    fun saveOrder(order: Order){
        repositoryFood.saveOrder(order)
    }
}
