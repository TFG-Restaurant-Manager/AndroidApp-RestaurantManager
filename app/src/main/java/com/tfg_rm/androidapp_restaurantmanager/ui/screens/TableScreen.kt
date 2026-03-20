package com.tfg_rm.androidapp_restaurantmanager.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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

@Composable
fun TableScreen(
    viewModel: TableViewModel = hiltViewModel(),
    goToAddOrders: () -> Unit = {}
) {
    LaunchedEffect(Unit) {
        viewModel.getTables()
    }
    val actualSection = remember { mutableIntStateOf(2) }
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
            val cards = viewModel.getTableInfo(actualSection.intValue)
            val sectionsList = viewModel.getSections()
            Column(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF59E0B), RoundedCornerShape(0.dp))
                        .statusBarsPadding()
                        .padding(horizontal = 16.dp)
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = stringResource(R.string.tablescreen_tablesmap).uppercase(Locale.getDefault()),
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
                        Spacer(modifier = Modifier.height(10.dp))
                        Divider(color = Color.White.copy(alpha = 0.18f), thickness = 1.dp)
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
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
                        tables = tables,
                        onTableClick = { tablesDto ->
                            viewModel.setTable(tablesDto.id)
                            goToAddOrders()
                        }
                    )
                }
            }
        }
    }

}

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

    val maxX = tables.maxOf { it.posX }
    val maxY = tables.maxOf { it.posY }

    val mapWidthPx = marginPx + (maxX + 1) * (sizeTablePx + marginPx)
    val mapHeightPx = marginPx + (maxY + 1) * (sizeTablePx + marginPx)


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

                    val minX = minOf(0f, viewportSize.width - mapWidthPx)
                    val minY = minOf(0f, viewportSize.height - mapHeightPx)

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
                val bgColor = if (table.available) Color(0xFF4CAF50) else Color(0xFFBDBDB)
                val borderColor = if (table.available) Color(0xFF2E7D32) else Color(0xFF9E9E9E)
                val textColor = if (table.available) Color.White else Color.DarkGray

                Box(
                    modifier = Modifier
                        .size(sizeTable)
                        .offset(
                            x = margin + (table.posX) * (sizeTable + margin),
                            y = margin + (table.posY) * (sizeTable + margin)
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
                    Text(
                        text = table.capacity.toString(),
                        style = MaterialTheme.typography.titleMedium,
                        color = textColor,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

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

@Composable
fun SelectSection(actualSection: MutableIntState, sectionsList: List<String>) {
    var expanded by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    ) {
        Text(
            text = stringResource(R.string.tablescreen_selectsection),
            style = MaterialTheme.typography.titleSmall
        )
        Box(
            modifier = Modifier
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
                text = sectionsList[(actualSection.intValue - 1)],
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
                        actualSection.intValue = name.last().toString().toInt()
                        expanded = false
                    }
                )
            }
        }
    }
}
