package com.tfg_rm.androidapp_restaurantmanager.data.repository

import com.tfg_rm.androidapp_restaurantmanager.data.remote.datasource.TablesOrdersDataSource
import com.tfg_rm.androidapp_restaurantmanager.data.remote.dto.TablesOrdersDto
import com.tfg_rm.androidapp_restaurantmanager.data.remote.mapper.toOrderItem
import com.tfg_rm.androidapp_restaurantmanager.domain.models.OrderItem
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository in charge of managing the combined data of tables and their respective orders.
 *
 * This class implements a simple in-memory caching strategy to minimize redundant network
 * requests. It serves as a shared data source for other repositories that need to
 * extract specific information about tables or orders.
 *
 * @property remote The remote data source used to fetch the unified table and order information.
 */
@Singleton
class TablesOrdersRepository @Inject constructor(
    private val remote: TablesOrdersDataSource
) {

    /** * In-memory cache to store the last fetched list of [TablesOrders].
     */
    private var cache: List<TablesOrders>? = null

    /**
     * Retrieves the list of tables and their orders, using the cache if available.
     *
     * If the [cache] is null, it performs a network request, maps the DTOs to domain
     * models, and populates the cache before returning the data.
     *
     * @return A list of [TablesOrders] domain objects.
     * @throws Exception If the remote data source fails.
     */
    suspend fun getTablesAndOrders(): List<TablesOrders> {
        return cache ?: remote.getTablesOrders().map { it.toTablesOrders() }.also {
            cache = it
        }
    }

    /**
     * Invalidates the current cache, forcing the next call to [getTablesAndOrders]
     * to fetch fresh data from the remote source.
     */
    fun clearCache() {
        cache = null
    }
}

/**
 * Extension of the [TablesOrdersDto] model to facilitate conversion to a [TablesOrders] domain object.
 *
 * This function performs the deep mapping of the combined data, including the nested
 * conversion of order items using [toOrderItem].
 *
 * @return A [TablesOrders] instance with the processed and mapped data.
 */
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

/**
 * Internal domain model representing a unified view of a restaurant table and its active order.
 *
 * @property tableId Unique identifier of the table.
 * @property capacity Seating capacity of the table.
 * @property posX X-coordinate for UI positioning.
 * @property posY Y-coordinate for UI positioning.
 * @property status Current occupancy status of the table.
 * @property sectionTitle Name of the area where the table is located.
 * @property orderId ID of the active order, or null if empty.
 * @property orderStatus Status of the order (e.g., "PENDING").
 * @property orderTotal Total amount of the current ticket.
 * @property orderNotes General comments regarding the order.
 * @property orderCreatedAt Timestamp of the order creation.
 * @property orderItems List of products included in the order.
 */
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