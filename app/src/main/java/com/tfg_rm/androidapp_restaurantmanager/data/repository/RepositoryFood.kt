package com.tfg_rm.androidapp_restaurantmanager.data.repository

import com.tfg_rm.androidapp_restaurantmanager.domain.models.Dishes
import com.tfg_rm.androidapp_restaurantmanager.domain.models.Order
import com.tfg_rm.androidapp_restaurantmanager.domain.models.Orders
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepositoryFood @Inject constructor() {

    suspend fun getDishes(): List<Dishes> {
        return listOf(
            Dishes(1, "Puré de patata", "Patatas cocidas con varios condimentos", "Principales", 8.5, true),
            Dishes(2, "Ensalada César", "Lechuga romana con pollo, crutones y salsa césar", "Entrantes", 7.5, true),
            Dishes(3, "Croquetas caseras", "Croquetas cremosas de jamón", "Entrantes", 6.0, true),
            Dishes(4, "Carpaccio de ternera", "Finas láminas de ternera con parmesano y aceite de oliva", "Entrantes", 9.0, true),
            Dishes(5, "Solomillo al Roquefort", "Solomillo de ternera con salsa de queso Roquefort", "Principales", 18.5, true),
            Dishes(6, "Lubina a la sal", "Lubina horneada con costra de sal", "Principales", 17.0, true),
            Dishes(7, "Paella valenciana", "Paella tradicional de pollo y verduras", "Principales", 15.0, true),
            Dishes(8, "Risotto de setas", "Arroz cremoso con setas y parmesano", "Principales", 14.0, true),
            Dishes(9, "Tarta de queso", "Tarta cremosa de queso al horno", "Postres", 5.5, true),
            Dishes(10, "Tiramisú", "Postre italiano con café y cacao", "Postres", 6.0, true),
            Dishes(11, "Coulant de chocolate", "Bizcocho de chocolate con corazón fundente", "Postres", 6.5, true),
            Dishes(12, "Agua mineral", "Botella de agua mineral", "Bebidas", 2.0, true),
            Dishes(13, "Vino tinto crianza", "Copa de vino tinto crianza", "Bebidas", 4.5, true),
            Dishes(14, "Cerveza", "Cerveza fría", "Bebidas", 3.0, true)
        )
    }

    fun saveOrder(order: Order){
        //ddd
    }
}
