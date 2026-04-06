package com.tfg_rm.androidapp_restaurantmanager.data.remote.mapper

import com.tfg_rm.androidapp_restaurantmanager.data.remote.dto.EmployeesDto
import com.tfg_rm.androidapp_restaurantmanager.domain.models.Employee
import java.time.LocalDateTime

/**
 * Extension of the [EmployeesDto] model to facilitate conversion to an [Employee] domain object.
 *
 * This mapping function transforms the data structure received from the API into a domain model.
 * It also processes the schedule data by sorting shifts in descending order based on their
 * start time and converting date strings into [LocalDateTime] pairs.
 *
 * @return An [Employee] instance containing the processed and mapped data from the DTO.
 */
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