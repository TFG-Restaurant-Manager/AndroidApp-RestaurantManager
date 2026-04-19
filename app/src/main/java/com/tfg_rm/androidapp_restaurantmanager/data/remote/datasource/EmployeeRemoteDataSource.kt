package com.tfg_rm.androidapp_restaurantmanager.data.remote.datasource

import com.tfg_rm.androidapp_restaurantmanager.data.remote.dto.EmployeesDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import javax.inject.Inject

class EmployeeRemoteDataSource @Inject constructor(
    private val client: HttpClient
) {
    suspend fun getEmployeeData(): EmployeesDto {
        val response = client.get("api/employee/me")
        return response.body()
    }
}