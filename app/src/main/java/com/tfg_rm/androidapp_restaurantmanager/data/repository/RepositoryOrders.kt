package com.tfg_rm.androidapp_restaurantmanager.data.repository

import com.tfg_rm.androidapp_restaurantmanager.data.remote.datasource.OrderRemoteDataSource
import com.tfg_rm.androidapp_restaurantmanager.data.remote.network.TokenProvider
import com.tfg_rm.androidapp_restaurantmanager.domain.models.Order
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository in charge of managing and consolidating order data within the application.
 *
 * This class acts as a high-level data provider that filters and transforms raw table-order
 * information into distinct [Order] domain models. It leverages a combined data source
 * to provide a coherent view of all active orders.
 *
 * @property remote The remote data source for direct order operations.
 * @property dataDouble The repository providing combined table and order data (TablesOrdersRepository).
 * @property tokenProvider The provider responsible for managing authentication tokens.
 */
@Singleton
class RepositoryOrders @Inject constructor(
    private val remote: OrderRemoteDataSource,
    private val dataDouble: TablesOrdersRepository,
    private val tokenProvider: TokenProvider
) {

    /**
     * Retrieves all currently active orders by processing the combined table-order data.
     *
     * It filters for tables that have an active order, groups the results by order ID,
     * and maps the raw data into [Order] domain objects, including status, total, and items.
     *
     * @return A list of [Order] domain objects currently active in the system.
     * @throws Exception If there is an error during data retrieval or date parsing.
     */
    suspend fun getOrders(): List<Order> {
        return dataDouble.getTablesAndOrders()
            .filter { it.orderId != null }
            .groupBy { it.orderId }
            .map { (_, items) ->

                val first = items.first()

                Order(
                    id = first.orderId!!,
                    tableId = first.tableId,
                    status = first.orderStatus!!,
                    total = first.orderTotal!!,
                    notes = first.orderNotes,
                    createdAt = LocalDateTime.parse(first.orderCreatedAt),
                    orderItemsList = first.orderItems!!.toMutableList()
                )
            }
    }

    /**
     * Clears the cached data in the underlying combined repository.
     * * This ensures that subsequent calls to [getOrders] will fetch fresh data
     * rather than returning stale information.
     */
    fun clearCache() {
        dataDouble.clearCache()
    }
}
