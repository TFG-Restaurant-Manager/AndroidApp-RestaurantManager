package com.tfg_rm.androidapp_restaurantmanager.data.remote.dto

data class TablesDto(
    val id : Int,
    val capacity : Int,
    val section : Int,
    val posX : Int,
    val posY : Int,
    val active : Boolean
)
