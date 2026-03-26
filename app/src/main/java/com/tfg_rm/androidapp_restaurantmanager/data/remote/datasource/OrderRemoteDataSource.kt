package com.tfg_rm.androidapp_restaurantmanager.data.remote.datasource

import android.util.Log
import com.tfg_rm.androidapp_restaurantmanager.data.remote.dto.OrderDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import javax.inject.Inject

class OrderRemoteDataSource @Inject constructor(
    private val client: HttpClient
) {
    suspend fun getOrders(): List<OrderDto> {
        val response = client.get("api/employee/getRestaurantTableOrders")
        Log.i("OrderRemoteDataSource", "Respuesta: ${response.status}")
        Log.i("OrderRemoteDataSource", "Contenido: ${response.bodyAsText()}")
        return response.body()
    }
}