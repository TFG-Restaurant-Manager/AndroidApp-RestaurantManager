package com.tfg_rm.androidapp_restaurantmanager.data.repository

import com.tfg_rm.androidapp_restaurantmanager.data.remote.datasource.EmployeeRemoteDataSource
import com.tfg_rm.androidapp_restaurantmanager.data.remote.mapper.toEmployee
import com.tfg_rm.androidapp_restaurantmanager.data.remote.network.TokenProvider
import com.tfg_rm.androidapp_restaurantmanager.domain.models.Employee
import javax.inject.Inject

class EmployeeRepository @Inject constructor(
    private val tokenProvider: TokenProvider,
    private val remote: EmployeeRemoteDataSource
) {
    suspend fun getEmployeeData(): Employee {
        return remote.getEmployeeData().toEmployee()
    }
}