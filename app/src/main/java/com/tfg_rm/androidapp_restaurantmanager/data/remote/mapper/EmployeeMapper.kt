package com.tfg_rm.androidapp_restaurantmanager.data.remote.mapper

import com.tfg_rm.androidapp_restaurantmanager.data.remote.dto.EmployeesDto
import com.tfg_rm.androidapp_restaurantmanager.domain.models.Employee
import java.time.LocalDateTime

fun EmployeesDto.toEmployee(): Employee {
    return Employee(
        roleName = this.roleName,
        name = this.name,
        email = this.email,
        phone = this.phone,
        schedules = this.schedules.sortedByDescending { it.startDatetime }
            .map { LocalDateTime.parse(it.startDatetime) to LocalDateTime.parse(it.endDatetime) }
    )
}