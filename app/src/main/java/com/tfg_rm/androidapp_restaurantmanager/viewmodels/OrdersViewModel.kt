package com.tfg_rm.androidapp_restaurantmanager.viewmodels

import androidx.compose.ui.res.stringResource
import com.tfg_rm.androidapp_restaurantmanager.R
import com.tfg_rm.androidapp_restaurantmanager.data.models.Order

class OrdersViewModel() {
    val ordersList: MutableList<Order> = mutableListOf()


    fun getStatus(order: Order): Int{
        val status: Int = when (order.statusId){
            1 -> R.string.orderStatusCreated
            2 -> R.string.orderStatusCooked
            3 -> R.string.orderStatusDelivered
            4 -> R.string.orderStatusPaid
            else -> R.string.orderStatusError
        }

        return status
    }


}