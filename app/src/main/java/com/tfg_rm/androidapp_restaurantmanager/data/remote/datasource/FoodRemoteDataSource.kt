package com.tfg_rm.androidapp_restaurantmanager.data.remote.datasource

import com.tfg_rm.androidapp_restaurantmanager.data.remote.dto.DishesDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import javax.inject.Inject

class FoodRemoteDataSource @Inject constructor(
    private val client: HttpClient
) {
    suspend fun getDishes(): List<DishesDto> {
        val response = client.get("/employee/getRestaurantDishes")

        println("STATUS: ${response.status}")
        val text = response.bodyAsText()
        println("BODY: $text")

        return response.body()
    }
}