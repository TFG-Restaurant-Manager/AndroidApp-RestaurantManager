package com.tfg_rm.androidapp_restaurantmanager.ui.screens

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.tfg_rm.androidapp_restaurantmanager.R
import com.tfg_rm.androidapp_restaurantmanager.domain.models.TableInfoUi
import com.tfg_rm.androidapp_restaurantmanager.domain.models.Tables
import com.tfg_rm.androidapp_restaurantmanager.domain.models.UiState
import com.tfg_rm.androidapp_restaurantmanager.domain.viewmodels.TableViewModel
import java.util.Locale
import kotlin.math.roundToInt

@Preview(showBackground = true)
@Composable
fun TableScreenPreview() {
    TableScreen()
}

/**
 * Main screen for the restaurant floor plan and table management.
 * It manages the lifecycle of table data using [TableViewModel], handling:
 * - **Idle/Loading:** Initiates data fetching and shows a progress spinner.
 * - **Error:** Displays a localized error message with a retry option.
 * - **Success:** Renders the interactive [TableMap], section selection, and a statistical
 * summary of table availability (Available/Occupied/Total).
 * @param viewModel ViewModel providing the table state and section logic.
 * @param goToAddOrders Navigation callback to transition to the food ordering screen.
 */
@Composable
fun TableScreen(
    viewModel: TableViewModel = hiltViewModel(),
    goToAddOrders: () -> Unit = {}
) {
    val tableState by viewModel.tables.collectAsState()

    when (tableState) {
        is UiState.Idle -> {
            Text("Idle")
            LaunchedEffect(Unit) {
                viewModel.getTables()
            }
        }

        is UiState.Loading -> LoadingScreen(stringResource(R.string.tablescreen_loading))
        is UiState.Error -> {
            val error = (tableState as UiState.Error).message
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
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(stringResource(error))
                        Button(
                            onClick = { viewModel.getTables() },
                            modifier = Modifier.width(200.dp)
                        ) {
                            Text("Recargar")
                        }
                    }
                }
            }
        }

        is UiState.Success<*> -> {
            val tables = (tableState as UiState.Success<List<Tables>>).data
            tables.forEach { Log.i("Mesas", it.toString()) }
            val sectionsList = viewModel.getSections(tables)
            val actualSection = remember { mutableStateOf(sectionsList[0]) }
            val cards = viewModel.getTableInfo(actualSection.value, tables)
            val helpExpanded = remember { mutableStateOf(false) }
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFF59E0B), RoundedCornerShape(0.dp))
                            .statusBarsPadding()
                            .padding(horizontal = 16.dp, vertical = 16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = stringResource(R.string.tablescreen_tablesmap).uppercase(
                                        Locale.getDefault()
                                    ),
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 20.sp
                                    ),
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = stringResource(R.string.tablescreen_selectsectiontable),
                                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
                                    color = Color.White.copy(alpha = 0.9f)
                                )
                            }
                            IconButton(
                                onClick = { helpExpanded.value = !helpExpanded.value }
                            ) {
                                Icon(
                                    Icons.AutoMirrored.Filled.HelpOutline,
                                    null,
                                    tint = Color.White
                                )
                            }
                        }
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        SelectSection(actualSection, sectionsList)

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
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

                        TableMap(
                            tables = tables.filter { it.section == actualSection.value },
                            onTableClick = { tablesDto ->
                                viewModel.setTable(tablesDto.id)
                                goToAddOrders()
                            }
                        )
                    }
                }
                if (helpExpanded.value) {
                    HelpOverlay(
                        modifier = Modifier.align(Alignment.TopEnd),
                        onClose = { helpExpanded.value = false }
                    )
                }
            }
        }
    }

}

/**
 * An interactive, draggable map representing the physical layout of the restaurant tables.
 *
 * Features:
 * - **Dynamic Positioning:** Renders tables based on their relative X/Y coordinates.
 * - **Gestures:** Supports drag/pan gestures to navigate large floor plans within a limited viewport.
 * - **Status-based Styling:** Tables change color (Green/Grey) based on their availability.
 * - **Clamping:** Ensures the map cannot be dragged beyond its calculated boundaries.
 * @param tables List of [Tables] domain objects to be rendered in the current view.
 * @param onTableClick Callback triggered when a specific table is selected.
 */
@Composable
fun TableMap(
    tables: List<Tables>,
    onTableClick: (Tables) -> Unit
) {
    var offset by remember { mutableStateOf(Offset.Zero) }
    var viewportSize by remember { mutableStateOf(IntSize.Zero) }

    val sizeTable = 60.dp
    val margin = 16.dp

    val density = LocalDensity.current

    val sizeTablePx = with(density) { sizeTable.toPx() }
    val marginPx = with(density) { margin.toPx() }

    val minX = tables.minOf { it.posX }
    val minY = tables.minOf { it.posY }

    val maxX = tables.maxOf { it.posX }
    val maxY = tables.maxOf { it.posY }

    val mapWidthPx = marginPx + (maxX - minX + 1) * (sizeTablePx + marginPx)
    val mapHeightPx = marginPx + (maxY - minY + 1) * (sizeTablePx + marginPx)


    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 16.dp)
            .clipToBounds()
            .onSizeChanged { viewportSize = it }
            .pointerInput(mapWidthPx, mapHeightPx, viewportSize) {
                detectDragGestures { _, drag ->
                    //offset += drag

                    val newX = offset.x + drag.x
                    val newY = offset.y + drag.y

                    val minX = minOf(0f, viewportSize.width.toFloat() - mapWidthPx.toFloat())
                    val minY = minOf(0f, viewportSize.height.toFloat() - mapHeightPx.toFloat())

                    offset = Offset(
                        x = newX.coerceIn(minX, 0f),
                        y = newY.coerceIn(minY, 0f)
                    )
                }
            }
            .border(
                width = 1.dp,
                shape = RoundedCornerShape(2.dp),
                color = Color.Black
            )
    ) {
        // Definimos un área grande donde se posicionan todas las mesas
        Box(
            modifier = Modifier
                //.width(margin + (tables.maxOf { it.posX }) * (sizeTable + margin))
                //.height(margin + (tables.maxOf { it.posX
                // }) * (sizeTable + margin))
                .offset {
                    IntOffset(
                        offset.x.roundToInt(),
                        offset.y.roundToInt()
                    )
                }
        ) {
            tables.forEach { table ->
                val bgColor =
                    if (table.status.equals("AVAILABLE")) Color(0xFF4CAF50) else Color(0xFFBDBDB)
                val borderColor =
                    if (table.status.equals("AVAILABLE")) Color(0xFF2E7D32) else Color(0xFF9E9E9E)
                val textColor =
                    if (table.status.equals("AVAILABLE")) Color.White else Color.DarkGray

                Box(
                    modifier = Modifier
                        .size(sizeTable)
                        .offset(
                            x = margin + (table.posX - minX) * (sizeTable + margin),
                            y = margin + (table.posY - minY) * (sizeTable + margin)
                        )
                        .border(
                            width = 2.dp,
                            color = borderColor,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .background(
                            color = bgColor,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable { onTableClick(table) },
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "${table.capacity} ${stringResource(R.string.tablescreen_peoplepertable_little)}",
                            style = MaterialTheme.typography.titleMedium,
                            color = textColor,
                            fontWeight = FontWeight.Bold,
                        )
                        Text(
                            text = "${stringResource(R.string.tablescreen_tablenumber_little)} ${table.id}",
                            style = MaterialTheme.typography.titleMedium,
                            color = textColor,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
            }
        }
    }
}

/**
 * A small summary card displaying occupancy statistics.
 * @param infoUi Data model containing the title, count, and color theme for the card.
 * @param modifier Modifier for sizing and layout constraints (usually for weight in a Row).
 */
@Composable
fun InformationCard(
    infoUi: TableInfoUi,
    modifier: Modifier = Modifier,
) {

    Card(
        modifier = modifier,
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = infoUi.count.toString(),
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = stringResource(infoUi.title),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

/**
 * A dropdown-based selector to filter tables by restaurant section (e.g., Terrace, Main Hall).
 * @param actualSection Mutable state holding the currently selected section name.
 * @param sectionsList List of available unique section names extracted from the table data.
 */
@Composable
fun SelectSection(actualSection: MutableState<String>, sectionsList: List<String>) {
    var expanded by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.tablescreen_selectsection),
            style = MaterialTheme.typography.titleMedium
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true }
                .height(40.dp)
                .width(100.dp)
                .background(Color(0xFFF7F7F7), RoundedCornerShape(8.dp))
                .border(width = 1.dp, color = Color(0xFFDDDDDD), shape = RoundedCornerShape(8.dp))
                .padding(horizontal = 8.dp)
        ) {
            Text(
                modifier = Modifier.align(alignment = Alignment.Center),
                textAlign = TextAlign.Center,
                text = actualSection.value,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF333333)
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
                        actualSection.value = name
                        expanded = false
                    }
                )
            }
        }
    }
}

/**
 * An overlay window providing a legend and help documentation for the table map.
 * Explains UI abbreviations (e.g., people per table, table numbers) and allows the
 * user to close it by clicking outside or on the close button.
 * @param modifier Alignment and padding modifiers for the overlay box.
 * @param onClose Callback to dismiss the help dialog.
 */
@Composable
fun HelpOverlay(
    modifier: Modifier = Modifier,
    onClose: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.15f))
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                onClose()
            }
    ) {
        Box(
            modifier = modifier
                .align(Alignment.TopEnd)
                .padding(top = 80.dp, end = 16.dp)
                .background(Color.White, RoundedCornerShape(12.dp))
                .border(1.dp, Color.Gray, RoundedCornerShape(12.dp))
                .padding(12.dp)
        ) {
            Column {
                Text("Leyenda:", fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    "${stringResource(R.string.tablescreen_peoplepertable_little)}: ${
                        stringResource(
                            R.string.tablescreen_peoplepertable
                        )
                    }"
                )
                Text("${stringResource(R.string.tablescreen_tablenumber_little)}: ${stringResource(R.string.tablescreen_tablenumber)}")

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Cerrar",
                    color = Color.Blue,
                    modifier = Modifier
                        .align(Alignment.End)
                        .clickable { onClose() }
                )
            }
        }
    }
}
