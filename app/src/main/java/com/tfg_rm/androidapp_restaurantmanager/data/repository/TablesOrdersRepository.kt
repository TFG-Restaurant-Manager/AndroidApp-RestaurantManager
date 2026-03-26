package com.tfg_rm.androidapp_restaurantmanager.data.repository

import com.tfg_rm.androidapp_restaurantmanager.data.remote.datasource.TablesOrdersDataSource
import com.tfg_rm.androidapp_restaurantmanager.data.remote.dto.TablesOrdersDto
import com.tfg_rm.androidapp_restaurantmanager.data.remote.mapper.toOrderItem
import com.tfg_rm.androidapp_restaurantmanager.domain.models.OrderItem
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TablesOrdersRepository @Inject constructor(
    private val remote: TablesOrdersDataSource
) {

    private var cache: List<TablesOrders>? = null

    suspend fun getTablesAndOrders(): List<TablesOrders> {
        return cache ?: remote.getTablesOrders().map { it.toTablesOrders() }.also {
            cache = it
        }
    }

    fun clearCache() {
        cache = null
    }
}

fun TablesOrdersDto.toTablesOrders(): TablesOrders {
    return TablesOrders(
        tableId = this.tableId,
        capacity = this.capacity,
        posX = this.posX,
        posY = this.posY,
        status = this.status,
        sectionTitle = this.sectionTitle,
        orderId = this.orderId,
        orderStatus = this.orderStatus,
        orderTotal = this.orderTotal,
        orderNotes = this.orderNotes,
        orderCreatedAt = this.orderCreatedAt,
        orderItems = this.orderItems?.map { it.toOrderItem() }
    )
}

data class TablesOrders(
    val tableId: Int,
    val capacity: Int,
    val posX: Double,
    val posY: Double,
    val status: String,
    val sectionTitle: String,
    val orderId: Int?,
    val orderStatus: String?,
    val orderTotal: Double?,
    val orderNotes: String?,
    val orderCreatedAt: String?,
    val orderItems: List<OrderItem>?
)