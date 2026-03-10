package com.tfg_rm.androidapp_restaurantmanager.data.remote

import com.tfg_rm.androidapp_restaurantmanager.data.remote.dto.Dishes
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.contentType

class ApiService {

    private val client = KtorClient.httpClient

    suspend fun getUsers(): List<Dishes> {
        return client.get("https://api.example.com/dishes") {
            contentType(ContentType.Application.Json)
        }.body()
    }
}