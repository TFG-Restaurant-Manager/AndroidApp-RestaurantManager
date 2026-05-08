package com.tfg_rm.androidapp_restaurantmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.tfg_rm.androidapp_restaurantmanager.ui.navigation.AppNavigation
import com.tfg_rm.androidapp_restaurantmanager.ui.theme.AndroidAppRestaurantManagerTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * The main entry point of the Android application.
 *
 * This activity is responsible for bootstrapping the application's UI and
 * initializing the dependency injection container through Hilt. It sets up
 * the top-level Compose content, including the custom theme and the
 * navigation graph.
 *
 * @see AppNavigation
 * @see AndroidAppRestaurantManagerTheme
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    /**
     * Called when the activity is first created.
     *
     * * [enableEdgeToEdge] is invoked to allow the layout to utilize the
     * full screen real estate, including the area behind system bars.
     * * [setContent] defines the UI composition, wrapping the [AppNavigation]
     * within the application's global [AndroidAppRestaurantManagerTheme].
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Configure the app to draw under system bars for a modern look
        enableEdgeToEdge()

        setContent {
            AndroidAppRestaurantManagerTheme {
                // Initialize the central navigation controller
                AppNavigation()
            }
        }
    }
}