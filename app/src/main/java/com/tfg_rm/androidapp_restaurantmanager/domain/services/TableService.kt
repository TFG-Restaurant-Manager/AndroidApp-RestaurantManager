package com.tfg_rm.androidapp_restaurantmanager.domain.services

import com.tfg_rm.androidapp_restaurantmanager.data.repository.RepositoryTable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TableService @Inject constructor(
    private val repositoryTable: RepositoryTable
) {

}
