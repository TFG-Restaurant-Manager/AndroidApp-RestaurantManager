package com.tfg_rm.androidapp_restaurantmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.tfg_rm.androidapp_restaurantmanager.navigation.AppNavigation
import com.tfg_rm.androidapp_restaurantmanager.ui.theme.AndroidAppRestaurantManagerTheme

/**
 * Clase principal del programa la cual inicializa el contenido mediante la navegacion
 * @see AppNavigation
 * Y con la configuracion del Thema predefinido
 * @see AndroidAppRestaurantManagerTheme
 *
 * @author Equipo Restaurant Manager
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Funcion para hacer que la aplicacion ocupe toda la pantalla
        enableEdgeToEdge()
        setContent {
            AndroidAppRestaurantManagerTheme {
                // Inicializo la navegacion
                AppNavigation()
            }
        }
    }
}