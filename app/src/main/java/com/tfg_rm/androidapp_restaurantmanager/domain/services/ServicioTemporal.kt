package com.tfg_rm.androidapp_restaurantmanager.domain.services

import com.tfg_rm.androidapp_restaurantmanager.data.repository.RepositoryTemporal
import com.tfg_rm.androidapp_restaurantmanager.domain.models.Dishes
import javax.inject.Inject

class ServicioTemporal @Inject constructor(
    private val repositoryTemporal: RepositoryTemporal
) {
    suspend  fun getUserName(): String {
        return repositoryTemporal.getUser()
    }

    suspend fun getDishes(): List<Dishes> {
        return repositoryTemporal.getDishes()
    }
}