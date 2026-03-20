package com.tfg_rm.androidapp_restaurantmanager.data.remote.datasource

import com.tfg_rm.androidapp_restaurantmanager.data.remote.dto.DishesDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import javax.inject.Inject

class FoodRemoteDataSource @Inject constructor(
    private val client: HttpClient
) {
    suspend fun getDishes(): List<DishesDto> {
        val response = client.get("api/employee/getRestaurantDishes")
        return response.body()
    }
}