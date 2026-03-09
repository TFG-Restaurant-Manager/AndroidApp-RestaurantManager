package com.tfg_rm.androidapp_restaurantmanager.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.tfg_rm.androidapp_restaurantmanager.controller.TableController
import com.tfg_rm.androidapp_restaurantmanager.data.models.Tables
import com.tfg_rm.androidapp_restaurantmanager.ui.models.TableInfoUi

@Preview(showBackground = true)
@Composable
fun TableScreenPreview() {
    TableScreen(
        navController = rememberNavController()
    )
}

@Composable
fun TableScreen(
    navController: NavController,
    controller: TableController = TableController()
) {
    val actualSection = remember { mutableIntStateOf(2) }
    val sectionsList = listOf("Seccion 1", "Seccion 2")
    //val cards = viewModel.getTableInfo(actualSection)
    val cards = getTableInfo(actualSection.intValue)

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp)) {
        Text(text = "Mapa de mesas",
            style = MaterialTheme.typography.titleMedium)
        Text(text = "Seleccione una sección y toque una mesa",
            style = MaterialTheme.typography.bodyMedium)

        SelectSection(actualSection, sectionsList)

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            cards.forEach { infoUi ->
                InformationCard(
                    infoUi = infoUi,
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                )
            }
        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                controller.goToFood(
                    navController,
                    1 /*Example value, in a future it will need to be
                    attached to a real id from a list of table items
                    */
                )
            }
        ) {
            Text("Example button of navigation to food screen")
        }
    }
}

@Composable
fun InformationCard(
    infoUi: TableInfoUi,
    modifier: Modifier = Modifier,
) {

    Card(modifier = modifier,
        colors = CardColors(
            containerColor = infoUi.color.copy(alpha = 0.2f),
            contentColor = infoUi.color,
            disabledContainerColor = infoUi.color,
            disabledContentColor = infoUi.color
        ),
        border = BorderStroke(
            width = 1.dp,
            color = infoUi.color
        )
    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .offset(y = (-8).dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = infoUi.count.toString(),
                style = MaterialTheme.typography.titleLarge)
            Text(text = infoUi.title,
                style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun SelectSection(actualSection: MutableIntState, sectionsList: List<String>) {
    var expanded by remember { mutableStateOf(false) }
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 8.dp)) {
        Text(text = "Seleccionar Sección",
            style = MaterialTheme.typography.titleSmall)
        Box(
            modifier = Modifier
                .clickable { expanded = true }
                .height(35.dp)
                .width(65.dp)
                .background(Color.LightGray, RoundedCornerShape(8.dp))
        ) {
            Text(
                modifier = Modifier.align(alignment = Alignment.Center),
                textAlign = TextAlign.Center,
                text = sectionsList[(actualSection.intValue-1)]
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            sectionsList.forEach { name ->
                DropdownMenuItem(
                    text = { Text(name) },
                    onClick = {
                        actualSection.intValue = name.last().toString().toInt()
                        expanded = false
                    }
                )
            }
        }
    }
}

// Mover a TablesViewModel
fun getTableInfo(section: Int): List<TableInfoUi> {
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

    val disponibles = tablesList.count { it.active && it.section == section }
    val ocupadas = tablesList.count { !it.active && it.section == section }
    val total = tablesList.count { it.section == section }

    return listOf(
        TableInfoUi(
            title = "Disponibles",
            count = disponibles,
            color = Color(0xFF4CAF50) // verde
        ),
        TableInfoUi(
            title = "Ocupadas",
            count = ocupadas,
            color = Color(0xFFF44336) // rojo
        ),
        TableInfoUi(
            title = "Total",
            count = total,
            color = Color(0xFF2196F3) // azul
        )
    )
}
