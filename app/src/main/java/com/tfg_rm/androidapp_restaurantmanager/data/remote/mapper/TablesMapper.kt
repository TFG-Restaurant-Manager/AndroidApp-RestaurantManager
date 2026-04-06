package com.tfg_rm.androidapp_restaurantmanager.data.remote.mapper

import com.tfg_rm.androidapp_restaurantmanager.data.remote.dto.TablesDto
import com.tfg_rm.androidapp_restaurantmanager.domain.models.Tables

/**
 * Extension of the [TablesDto] model to facilitate conversion to a [Tables] domain object.
 *
 * This mapping function transfers the physical and state data of a table from the
 * data transfer layer to the domain layer, allowing the application to manage
 * table positioning and status within its business logic.
 *
 * @return A [Tables] instance containing the corresponding data from the DTO.
 */
fun TablesDto.toTables(): Tables {
    return Tables(
        id = this.id,
        capacity = this.capacity,
        section = this.section,
        posX = this.posX,
        posY = this.posY,
        status = this.status
    )
}