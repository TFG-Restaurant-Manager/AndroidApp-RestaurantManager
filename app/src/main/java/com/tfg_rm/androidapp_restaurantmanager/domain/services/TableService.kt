package com.tfg_rm.androidapp_restaurantmanager.domain.services

import com.tfg_rm.androidapp_restaurantmanager.data.repository.RepositoryTable
import com.tfg_rm.androidapp_restaurantmanager.domain.models.Tables
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Domain service that manages business logic related to the restaurant's physical tables.
 * * This service acts as a clean entry point for the UI layer to access table data,
 * abstracting the mapping and filtering logic performed by the [RepositoryTable].
 *
 * @property repositoryTable The repository responsible for retrieving and coordinating table information.
 */
@Singleton
class TableService @Inject constructor(
    private val repositoryTable: RepositoryTable
) {

    /**
     * Retrieves the current list of tables, including their status and location coordinates.
     *
     * @return A list of [Tables] domain objects representing the restaurant's floor plan.
     * @throws Exception If there is a failure during data retrieval from the repository.
     */
    suspend fun getTables(): List<Tables> {
        return repositoryTable.getTables()
    }
}
