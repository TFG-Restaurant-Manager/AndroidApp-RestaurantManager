package com.tfg_rm.androidapp_restaurantmanager.data.repository

import com.tfg_rm.androidapp_restaurantmanager.data.remote.datasource.FoodRemoteDataSource
import com.tfg_rm.androidapp_restaurantmanager.data.remote.mapper.toDishes
import com.tfg_rm.androidapp_restaurantmanager.data.remote.network.TokenProvider
import com.tfg_rm.androidapp_restaurantmanager.domain.models.Dishes
import com.tfg_rm.androidapp_restaurantmanager.domain.models.Order
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepositoryFood @Inject constructor(
    private val remote: FoodRemoteDataSource,
    private val tokenProvider: TokenProvider
) {

    suspend fun getDishes(): List<Dishes> {
        return remote.getDishes().map { it.toDishes() }
    }

    fun saveOrder(order: Order) {
        //ddd
    }
}
