package com.tfg_rm.androidapp_restaurantmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.tfg_rm.androidapp_restaurantmanager.ui.navigation.AppNavigation
import com.tfg_rm.androidapp_restaurantmanager.ui.theme.AndroidAppRestaurantManagerTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Clase principal del programa la cual inicializa el contenido mediante la navegacion
 * @see AppNavigation
 * Y con la configuracion del Thema predefinido
 * @see AndroidAppRestaurantManagerTheme
 *
 * @author Equipo Restaurant Manager
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Funcion para hacer que la aplicacion ocupe toda la pantalla
        enableEdgeToEdge()
        // Ocultar la barra de estado (reloj, batería)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val insetsController = WindowCompat.getInsetsController(window, window.decorView)
        insetsController?.hide(WindowInsetsCompat.Type.statusBars())
        insetsController?.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        setContent {
            AndroidAppRestaurantManagerTheme {
                // Inicializo la navegacion
                AppNavigation()
            }
        }
    }
}