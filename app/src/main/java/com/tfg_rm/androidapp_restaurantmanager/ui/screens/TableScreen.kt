package com.tfg_rm.androidapp_restaurantmanager.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tfg_rm.androidapp_restaurantmanager.data.models.Tables

@Preview(showBackground = true)
@Composable
fun TableScreen() {
    val tablesList = listOf(
        Tables(id = 1, capacity = 4, section = 1, posX = 1, posY = 1, active = true),
        Tables(id = 3, capacity = 6, section = 1, posX = 1, posY = 2, active = true),
        Tables(id = 2, capacity = 2, section = 1, posX = 2, posY = 2, active = false),
        Tables(id = 4, capacity = 4, section = 1, posX = 2, posY = 1, active = true),

        Tables(id = 1, capacity = 3, section = 2, posX = 1, posY = 1, active = true),
        Tables(id = 3, capacity = 7, section = 2, posX = 1, posY = 2, active = true),
        Tables(id = 2, capacity = 1, section = 2, posX = 2, posY = 2, active = false),
        Tables(id = 4, capacity = 3, section = 2, posX = 2, posY = 1, active = true)
    )

    val actualSection : Int = 2

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = "Mapa de mesas",
            style = MaterialTheme.typography.titleMedium)
        Text(text = "Seleccione una sección y toque una mesa",
            style = MaterialTheme.typography.bodyMedium)

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
        ) {
            repeat(3) { status ->
                InformationCard(
                    tablesList = getTableInfo(status, actualSection),
                    modifier = Modifier.weight(1f).aspectRatio(1f))
            }
        }
    }
}

@Composable
fun InformationCard(
    tablesList: Pair<String, Int>,
    modifier: Modifier = Modifier,
) {

    Card(modifier = modifier,
        border = BorderStroke(
            width = 1.dp,
            color = Color.Red
        )
    ) {
        Text(tablesList.second.toString())
        Text(tablesList.first)
    }
}

// Mover a TablesViewModel
fun getTableInfo(status: Int, section : Int): Pair<String, Int> {
    val tablesList = listOf(
        Tables(id = 1, capacity = 4, section = 1, posX = 1, posY = 1, active = true),
        Tables(id = 3, capacity = 6, section = 1, posX = 1, posY = 2, active = true),
        Tables(id = 2, capacity = 2, section = 1, posX = 2, posY = 2, active = false),
        Tables(id = 4, capacity = 4, section = 1, posX = 2, posY = 1, active = true),

        Tables(id = 1, capacity = 3, section = 2, posX = 1, posY = 1, active = true),
        Tables(id = 3, capacity = 7, section = 2, posX = 1, posY = 2, active = true),
        Tables(id = 2, capacity = 1, section = 2, posX = 2, posY = 2, active = false),
        Tables(id = 4, capacity = 3, section = 2, posX = 2, posY = 1, active = true)
    )

    return when (status) {
        0 -> "Disponibles" to tablesList.count { it.active && it.section == section }
        1 -> "Ocupadas" to tablesList.count { !it.active && it.section == section }
        else -> "Total" to tablesList.count {it.section == section }
    }
}
