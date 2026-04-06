package com.tfg_rm.androidapp_restaurantmanager.domain.services

import com.tfg_rm.androidapp_restaurantmanager.data.repository.RepositoryTable
import com.tfg_rm.androidapp_restaurantmanager.domain.models.Tables
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TableService @Inject constructor(
    private val repository: RepositoryTable
) {
    suspend fun getTables(): List<Tables> {
        return repository.getTables()
    }

    val events = repository.events

    suspend fun sendUpdate(message: String) {
        repository.sendUpdate(message)
    }
}
