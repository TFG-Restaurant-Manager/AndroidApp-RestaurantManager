package com.tfg_rm.androidapp_restaurantmanager.data.repository

import com.tfg_rm.androidapp_restaurantmanager.data.remote.datasource.FoodRemoteDataSource
import com.tfg_rm.androidapp_restaurantmanager.data.remote.mapper.toDishes
import com.tfg_rm.androidapp_restaurantmanager.data.remote.network.TokenProvider
import com.tfg_rm.androidapp_restaurantmanager.domain.models.Dishes
import com.tfg_rm.androidapp_restaurantmanager.domain.models.Order
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository in charge of managing food-related data operations and order processing.
 *
 * This class acts as a bridge between the remote food data source and the domain layer,
 * handling the retrieval of the menu and providing a foundation for order management.
 *
 * @property remote The remote data source used to fetch dish and menu information.
 * @property tokenProvider The provider responsible for managing authentication tokens.
 */
@Singleton
class RepositoryFood @Inject constructor(
    private val remote: FoodRemoteDataSource,
    private val tokenProvider: TokenProvider
) {

    /**
     * Fetches the complete list of dishes from the remote server and maps them to domain models.
     *
     * @return A list of [Dishes] domain objects representing the current menu.
     * @throws Exception If the network request fails or data transformation errors occur.
     */
    suspend fun getDishes(): List<Dishes> {
        return remote.getDishes().map { it.toDishes() }
    }

    /**
     * Saves or submits an order to the system.
     *
     * @param order The [Order] domain object containing the details to be persisted or sent.
     */
    fun saveOrder(order: Order) {
        // Implementation pending
    }
}
