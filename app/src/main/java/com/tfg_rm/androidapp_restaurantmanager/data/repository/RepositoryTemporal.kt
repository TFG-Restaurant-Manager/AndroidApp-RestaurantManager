package com.tfg_rm.androidapp_restaurantmanager.data.repository

import javax.inject.Inject

class RepositoryTemporal @Inject constructor() {

    suspend fun getUser(): String {
        return "Usuarios desde RepositoryTemporal"
    }
}