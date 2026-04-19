package com.tfg_rm.androidapp_restaurantmanager.data.remote.datasource

import com.tfg_rm.androidapp_restaurantmanager.data.remote.dto.TablesOrdersDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import javax.inject.Inject

class TablesOrdersDataSource @Inject constructor(
    private val client: HttpClient
) {
    suspend fun getTablesOrders(): List<TablesOrdersDto> {
        return client.get("api/table").body()
    }
}