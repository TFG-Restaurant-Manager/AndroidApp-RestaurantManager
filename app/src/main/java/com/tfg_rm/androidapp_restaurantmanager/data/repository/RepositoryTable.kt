package com.tfg_rm.androidapp_restaurantmanager.data.repository

import com.tfg_rm.androidapp_restaurantmanager.data.remote.datasource.TableRemoteDataSource
import com.tfg_rm.androidapp_restaurantmanager.data.remote.network.TokenProvider
import com.tfg_rm.androidapp_restaurantmanager.domain.models.Tables
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository in charge of managing table-related data and their physical layout.
 *
 * This class coordinates the retrieval of restaurant tables by utilizing a consolidated
 * data source. It ensures that table information is unique and correctly mapped
 * from the combined table-order view into the domain representation.
 *
 * @property remote The remote data source for direct table operations.
 * @property dataDouble The repository providing combined table and order data (TablesOrdersRepository).
 * @property tokenProvider The provider responsible for managing authentication tokens.
 */
@Singleton
class RepositoryTable @Inject constructor(
    private val remote: TableRemoteDataSource,
    private val dataDouble: TablesOrdersRepository,
    private val tokenProvider: TokenProvider
) {

    /**
     * Retrieves the list of unique tables available in the restaurant.
     *
     * It fetches data from the combined repository, removes duplicates based on the
     * table ID, and transforms the result into [Tables] domain objects including
     * their capacity, section, and spatial coordinates.
     *
     * @return A list of [Tables] domain objects representing the restaurant's layout.
     * @throws Exception If there is an error during data retrieval.
     */
    suspend fun getTables(): List<Tables> {
        return dataDouble.getTablesAndOrders()
            .distinctBy { it.tableId }
            .map {
                Tables(
                    id = it.tableId,
                    capacity = it.capacity,
                    section = it.sectionTitle,
                    posX = it.posX,
                    posY = it.posY,
                    status = it.status
                )
            }
    }
}
