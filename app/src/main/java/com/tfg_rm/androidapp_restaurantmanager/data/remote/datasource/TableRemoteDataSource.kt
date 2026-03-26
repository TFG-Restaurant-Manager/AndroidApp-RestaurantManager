package com.tfg_rm.androidapp_restaurantmanager.data.remote.datasource

import com.tfg_rm.androidapp_restaurantmanager.data.remote.dto.TablesDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import javax.inject.Inject

class TableRemoteDataSource @Inject constructor(
    private val client: HttpClient
) {
    suspend fun getTables(): List<TablesDto> {
        return client.get("").body()
    }

    suspend fun getSections(): List<String> {
        return client.get("").body()
    }
}