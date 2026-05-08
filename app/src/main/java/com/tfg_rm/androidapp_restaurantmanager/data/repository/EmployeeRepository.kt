package com.tfg_rm.androidapp_restaurantmanager.data.repository

import com.tfg_rm.androidapp_restaurantmanager.data.remote.datasource.EmployeeRemoteDataSource
import com.tfg_rm.androidapp_restaurantmanager.data.remote.mapper.toEmployee
import com.tfg_rm.androidapp_restaurantmanager.data.remote.network.TokenProvider
import com.tfg_rm.androidapp_restaurantmanager.domain.models.Employee
import javax.inject.Inject

/**
 * Repository in charge of managing employee-related data operations.
 * * This class acts as a mediator between the remote data source and the domain layer,
 * retrieving employee information and transforming it into a domain-usable model.
 *
 * @property tokenProvider The provider responsible for managing authentication tokens (unused in this specific method but provided via injection).
 * @property remote The remote data source used to fetch employee details from the API.
 */
class EmployeeRepository @Inject constructor(
    private val tokenProvider: TokenProvider,
    private val remote: EmployeeRemoteDataSource
) {
    /**
     * Fetches the current employee's data from the remote server and maps it to a domain model.
     *
     * @return An [Employee] domain object containing the profile and schedule information.
     * @throws Exception If the network request fails or the response cannot be parsed.
     */
    suspend fun getEmployeeData(): Employee {
        return remote.getEmployeeData().toEmployee()
    }
}