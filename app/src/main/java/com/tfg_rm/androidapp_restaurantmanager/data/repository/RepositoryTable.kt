package com.tfg_rm.androidapp_restaurantmanager.data.repository

import com.tfg_rm.androidapp_restaurantmanager.data.remote.datasource.TableRemoteDataSource
import com.tfg_rm.androidapp_restaurantmanager.data.remote.network.TokenProvider
import com.tfg_rm.androidapp_restaurantmanager.data.remote.network.WebSocketManager
import com.tfg_rm.androidapp_restaurantmanager.domain.models.Tables
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepositoryTable @Inject constructor(
    private val remote: TableRemoteDataSource,
    private val dataDouble: TablesOrdersRepository,
    private val tokenProvider: TokenProvider,
    private val webSocketManager: WebSocketManager
) {

    suspend fun getTables(): List<Tables> {
        return dataDouble.getTablesAndOrders()
            .distinctBy { it.tableId }
            .map {
                Tables(
                    id = it.tableId,
                    capacity = it.capacity,
                    section = it.sectionTitle,
                    posX = it.posX,
                    posY = it.posY,
                    status = it.status
                )
            }
    }

    val events = webSocketManager.events

    suspend fun sendUpdate(message: String) {
        webSocketManager.sendMessage(message)
    }
}
