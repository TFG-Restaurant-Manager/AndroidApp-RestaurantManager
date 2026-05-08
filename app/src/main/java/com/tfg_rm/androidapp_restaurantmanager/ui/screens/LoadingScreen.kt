package com.tfg_rm.androidapp_restaurantmanager.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/**
 * A reusable Composable screen used to display a standardized loading state across the app.
 * * It features a centered layout containing a [CircularProgressIndicator] and a
 * customizable message. This component is typically used as a placeholder while
 * asynchronous domain operations (like fetching orders or tables) are in progress.
 *
 * @param mensaje The text to be displayed below the loading spinner, providing
 * context to the user about what is being loaded.
 */
@Composable
fun LoadingScreen(mensaje: String) {
    // Box occupies the full screen and centers its children both horizontally and vertically
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Material 3 Circular progress indicator using the theme's primary color
            CircularProgressIndicator(
                modifier = Modifier.size(50.dp),
                color = MaterialTheme.colorScheme.primary,
                strokeWidth = 4.dp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Informative text for the user
            Text(
                text = mensaje,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )
        }
    }
}