package com.tfg_rm.androidapp_restaurantmanager.data.remote.mapper

import com.tfg_rm.androidapp_restaurantmanager.data.remote.dto.TablesDto
import com.tfg_rm.androidapp_restaurantmanager.domain.models.Tables

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