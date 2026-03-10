package com.tfg_rm.androidapp_restaurantmanager.data.remote.dto

data class Dishes(
    val id : Int,
    val name : String,
    val description : String,
    val category : String,
    val price : Double,
    val available : Boolean
)
