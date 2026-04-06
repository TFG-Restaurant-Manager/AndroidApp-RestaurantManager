package com.tfg_rm.androidapp_restaurantmanager.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.tfg_rm.androidapp_restaurantmanager.R
import com.tfg_rm.androidapp_restaurantmanager.domain.models.Dishes
import com.tfg_rm.androidapp_restaurantmanager.domain.models.Order
import com.tfg_rm.androidapp_restaurantmanager.domain.models.UiState
import com.tfg_rm.androidapp_restaurantmanager.domain.viewmodels.FoodViewModel
import com.tfg_rm.androidapp_restaurantmanager.domain.viewmodels.TableViewModel
import java.time.LocalDateTime
import java.util.Locale

@Preview(showBackground = true)
@Composable
fun DoOrderScreenPreview() {
    val mockDishes = listOf(
        Dishes(1, "Puré de patata", "Patatas cocidas", "Principales", 8.5, true),
        Dishes(2, "Ensalada César", "Pollo, crutones y salsa", "Entrantes", 7.5, false),
    )

    FoodContent(
        dishesCategories = listOf("Todo", "Entrantes", "Bebidas", "Principales", "Postres"),
        selectedCategory = "Todo",
        onCategorySelected = {},
        actualTable = "5",
        backToTables = {},
        getOrderDishesQuantity = { 0 }, // Devuelve un Int
        getOrderTotalAmount = { 0.0 }, // Devuelve un Double
        filterDishes = { mockDishes }, // Devuelve la lista filtrada
        onAddDish = {},
        onPlusDish = {},
        onMinusDish = {},
        onUpdateNote = { _, _ -> }, // Recibe dos parámetros
        isDishInOrder = { false }, // Devuelve un Boolean
        getDishQuantityInOrder = { 0 }, // Devuelve un Int
        getNotes = { "" }, // Devuelve un String
        isNoteEmpty = { true } // Devuelve un Boolean
        , onSendOrder = {}
    )
}

/**
 * Main entry point for the order creation process for a specific table.
 * Orchestrates the business logic between [FoodViewModel] and [TableViewModel].
 * It manages the dish selection, order aggregation, and provides a reactive
 * interface that handles:
 * - **Initial Fetching:** Automatically triggers dish loading if in [UiState.Idle].
 * - **Category Management:** Dynamically extracts categories from the loaded dishes.
 * - **State Sync:** Links the current order items, quantities, and notes to the UI.
 * @param viewModel ViewModel responsible for dish data and order item manipulation.
 * @param tableViewModel ViewModel providing context for the currently active table.
 * @param backToTables Navigation callback to return to the restaurant floor plan.
 */
@Composable
fun DoOrderScreen(
    viewModel: FoodViewModel = hiltViewModel(),
    tableViewModel: TableViewModel = hiltViewModel(),
    backToTables: () -> Unit = {}
) {
    val productosRestaurante by viewModel.dishes.collectAsState()
    val context = LocalContext.current
    val table by tableViewModel.actualTable
    when (val state = productosRestaurante) {
        is UiState.Idle -> {
            viewModel.getDishes()
        }

        is UiState.Loading -> LoadingScreen(stringResource(R.string.foodscreen_loading))
        is UiState.Success<List<Dishes>> -> {
            val dishes: List<Dishes> = state.data
            val dishesCategories: List<String> = viewModel.getDishesCategories(dishes)
            var selectedCategory by remember { mutableStateOf(dishesCategories[0]) }
            val order = remember {
                mutableStateOf(
                    Order(
                        1, 1, "CREATED", 0.0, null, LocalDateTime.now()
                    )
                )
            }
            val text = stringResource(R.string.foodscreen_order_sent)
            FoodContent(
                dishesCategories, selectedCategory,
                onCategorySelected = { selectedCategory = it },
                actualTable = table.toString(),
                backToTables = backToTables,
                getOrderDishesQuantity = { viewModel.getOrderDishesQuantity(order) },
                getOrderTotalAmount = { viewModel.getOrderTotalAmount(order) },
                filterDishes = { searchedDish ->
                    viewModel.filterDishes(
                        dishes,
                        searchedDish,
                        selectedCategory
                    )
                },
                onAddDish = { dish -> viewModel.addDishToOrder(order, dish) },
                onPlusDish = { dish -> viewModel.addDishToOrder(order, dish) },
                onMinusDish = { dish -> viewModel.minusDishOnOrder(order, dish) },
                onUpdateNote = { dish, newNote -> viewModel.updateNotes(dish, newNote, order) },
                isDishInOrder = { dish -> viewModel.isDishInOrder(order, dish) },
                getDishQuantityInOrder = { dish -> viewModel.getDishQuantityInOrder(order, dish) },
                getNotes = { dish -> viewModel.getNotes(dish, order) },
                isNoteEmpty = { dish -> viewModel.isNoteEmpty(dish, order) },
                onSendOrder = {
                    // Save order to repository (per table)
                    //viewModel.saveOrder(order.value)

                    Toast.makeText(
                        context,
                        text,
                        Toast.LENGTH_SHORT
                    ).show()
                    order.value = Order(
                        1, 1, "CREATED", 0.0, null, LocalDateTime.now()
                    )
                }
            )

        }

        is UiState.Error -> {
            val error = state.message
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
                            onClick = { viewModel.getDishes() },
                            modifier = Modifier.width(200.dp)
                        ) {
                            Text("Recargar")
                        }
                        Button(
                            onClick = { backToTables() },
                            modifier = Modifier.width(200.dp)
                        ) {
                            Text("Volver")
                        }
                    }
                }
            }
        }

        else -> {}
    }
}

/**
 * Structural layout of the order screen.
 * Uses a [Scaffold] to manage:
 * - **Top Bar:** Displays table context and navigation.
 * - **Bottom Bar:** Shows order summary (total/quantity) and the "Send to Kitchen" action.
 * - **Content:** Contains the search bar, category filter, and the scrollable dish list.
 */
@Composable
fun FoodContent(
    dishesCategories: List<String>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    actualTable: String,
    backToTables: () -> Unit,
    getOrderDishesQuantity: () -> Int,
    getOrderTotalAmount: () -> Double,
    filterDishes: (String) -> List<Dishes>,
    onAddDish: (Dishes) -> Unit,
    onPlusDish: (Dishes) -> Unit,
    onMinusDish: (Dishes) -> Unit,
    onUpdateNote: (Dishes, String) -> Unit,
    isDishInOrder: (Dishes) -> Boolean,
    getDishQuantityInOrder: (Dishes) -> Int,
    getNotes: (Dishes) -> String,
    isNoteEmpty: (Dishes) -> Boolean,
    onSendOrder: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopTableBar(
                "${stringResource(R.string.table)} $actualTable",
                onBack = { backToTables() })
        },
        bottomBar = {
            if (getOrderDishesQuantity() > 0) {
                BottomTableBar(
                    getOrderDishesQuantity = { getOrderDishesQuantity() },
                    getOrderTotalAmount = { getOrderTotalAmount() }, onSendOrder = { onSendOrder() }
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
        ) {
            var searchedDish by remember { mutableStateOf("") }

            OutlinedTextField(
                value = searchedDish,
                onValueChange = { searchedDish = it },
                placeholder = { Text(stringResource(R.string.foodscreen_search)) },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null)
                },
                singleLine = true,
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .fillMaxWidth()
            )
            Spacer(Modifier.padding(5.dp))
            CategorySelector(
                dishesCategories,
                selectedCategory,
                onCategorySelected = { onCategorySelected(it) }
            )

            DishesList(
                filterDishes(searchedDish),
                onAddDish = { dish -> onAddDish(dish) },
                onPlusDish = { dish -> onPlusDish(dish) },
                onMinusDish = { dish -> onMinusDish(dish) },
                isDishInOrder = { dish -> isDishInOrder(dish) },
                getDishQuantityInOrder = { dish -> getDishQuantityInOrder(dish) },
                getNotes = { dish -> getNotes(dish) },
                isNoteEmpty = { dish -> isNoteEmpty(dish) },
                onUpdateNote = { dish, newNote -> onUpdateNote(dish, newNote) })
        }
    }
}

/**
 * A persistent summary bar shown when at least one item is in the current order.
 * Displays the total item count and the final price, acting as a final check before
 * submitting the order to the backend/kitchen.
 */
@Composable
fun BottomTableBar(
    getOrderDishesQuantity: () -> Int,
    getOrderTotalAmount: () -> Double, onSendOrder: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RectangleShape,
        border = BorderStroke(1.dp, Color.LightGray)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("${getOrderDishesQuantity()} ${stringResource(R.string.products).lowercase()}")
                Text("${getOrderTotalAmount()} €")
            }
            Button(
                onClick = { onSendOrder() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF10B981),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(6.dp)
            ) {
                Text(stringResource(R.string.foodscreen_sendtokitchen))
            }
        }
    }
}

/**
 * A custom top navigation bar for the order creation screen.
 * It serves as a contextual header that displays the current table's name (e.g., "Table 5")
 * and provides a back navigation button to return to the restaurant's floor plan.
 * Designed to be lightweight and fit within the status bar padding for a seamless UI.
 * @param tableName The localized string representing the table currently being served.
 * @param onBack Callback function triggered when the user clicks the back arrow icon.
 */
@Composable
fun TopTableBar(tableName: String, onBack: () -> Unit) {
    Box(
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxWidth()
    ) {
        IconButton(
            onClick = { onBack() },
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back to Tables",
                tint = Color.Black,
                modifier = Modifier.size(14.dp)
            )
        }
        Text(
            tableName,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

/**
 * A horizontal scrolling selector for menu categories (e.g., Starters, Drinks, Desserts).
 * Provides visual feedback for the currently active category using distinct colors
 * and elevation to help the user filter the menu quickly.
 */
@Composable
fun CategorySelector(
    categories: List<String>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    HorizontalDivider(
        thickness = 1.dp,
        color = Color.LightGray
    )
    LazyRow(
        modifier = Modifier
            .padding(vertical = 7.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        contentPadding = PaddingValues(horizontal = 14.dp)
    ) {
        items(categories) { category ->
            Button(
                onClick = { onCategorySelected(category) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedCategory == category) Color(0xFFF59E0B) else Color.White,
                    contentColor = if (selectedCategory == category) Color.White else Color.DarkGray
                ),
                shape = RoundedCornerShape(6.dp),
                border = if (selectedCategory != category) BorderStroke(
                    1.dp,
                    Color.LightGray
                ) else null,
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 6.dp
                )
            ) {
                Text(category)
            }
        }
    }

    HorizontalDivider(
        thickness = 1.dp,
        color = Color.LightGray
    )
}

/**
 * A reactive list of menu items available for ordering.
 * Each item is rendered in a [Card] with logic to:
 * - **Add/Remove:** Increase or decrease quantities for items already in the order.
 * - **Availability Check:** Disables selection and shows an error badge if the dish is out of stock.
 * - **Notes Management:** Toggles an inline text field to add special kitchen instructions
 * (observations) for specific dishes.
 * @param dishes The filtered list of [Dishes] to display.
 * @param onUpdateNote Callback used to attach specific text instructions to a dish.
 */
@Composable
fun DishesList(
    dishes: List<Dishes>,
    onAddDish: (dish: Dishes) -> Unit,
    onPlusDish: (dish: Dishes) -> Unit,
    onMinusDish: (dish: Dishes) -> Unit,
    isDishInOrder: (dish: Dishes) -> Boolean,
    getDishQuantityInOrder: (dish: Dishes) -> Int,
    getNotes: (dish: Dishes) -> String,
    isNoteEmpty: (dish: Dishes) -> Boolean,
    onUpdateNote: (dish: Dishes, newNote: String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth()
    ) {
        items(dishes) { dish ->
            val notesOpen = remember { mutableStateOf(false) }
            Card(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                border = BorderStroke(1.dp, Color.LightGray),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 3.dp
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(dish.name)
                            Text(
                                String.format(Locale.getDefault(), "%.2f €", dish.price),
                                color = Color(0xFFF59E0B)
                            )

                        }
                        if (isDishInOrder(dish)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconButton(
                                    onClick = {
                                        onMinusDish(dish)
                                    }
                                ) {
                                    Text("-")
                                }
                                Text("${getDishQuantityInOrder(dish)}")
                                IconButton(
                                    onClick = {
                                        onPlusDish(dish)
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "Add product ${dish.name} to the order",
                                        tint = Color.Black,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            }
                        } else {
                            if (dish.available) {
                                Button(
                                    onClick = {
                                        onAddDish(dish)
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFFF59E0B)
                                    ),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "Add product ${dish.name} to the order",
                                        tint = Color.White,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(stringResource(R.string.add))
                                }
                            } else {
                                Card(
                                    shape = RoundedCornerShape(6.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = Color(0xFFEF5350)
                                    )
                                ) {
                                    Row(
                                        modifier = Modifier.padding(
                                            horizontal = 12.dp,
                                            vertical = 8.dp
                                        ),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "${dish.name} ${stringResource(R.string.foodscreen_noavailable)}",
                                            tint = Color.White,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = stringResource(R.string.foodscreen_noavailable),
                                            color = Color.White
                                        )
                                    }
                                }
                            }
                        }
                    }
                    if (isDishInOrder(dish)) {
                        HorizontalDivider(
                            thickness = 1.dp,
                            modifier = Modifier.fillMaxWidth()
                        )
                        if (!notesOpen.value) {
                            Button(
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.White
                                ),
                                shape = RoundedCornerShape(6.dp),
                                border = BorderStroke(1.dp, Color.LightGray),
                                onClick = {
                                    notesOpen.value = true
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Add product ${dish.name} to the order",
                                    tint = Color.DarkGray,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    stringResource(R.string.foodscreen_addobservations),
                                    color = Color.DarkGray
                                )
                            }
                        } else {
                            BasicTextField(
                                value = getNotes(dish),
                                onValueChange = { newValue ->
                                    onUpdateNote(dish, newValue)
                                },
                                modifier = Modifier
                                    .fillMaxWidth(),
                                singleLine = false,
                                decorationBox = { innerTextField ->
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .border(1.dp, Color.LightGray, RoundedCornerShape(6.dp))
                                            .padding(12.dp)
                                    ) {
                                        if (isNoteEmpty(dish)) {
                                            Text(
                                                text = stringResource(R.string.foodscreen_observationsexample),
                                                color = Color.Gray
                                            )
                                        }
                                        innerTextField()
                                    }
                                }
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Button(
                                    onClick = {
                                        notesOpen.value = false
                                    },
                                    modifier = Modifier.weight(0.6f),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFF10B981),
                                        contentColor = Color.White
                                    ),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(stringResource(R.string.save))
                                }

                                Button(
                                    onClick = {
                                        notesOpen.value = false
                                        onUpdateNote(dish, "")
                                    },
                                    modifier = Modifier.weight(0.4f),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color.White,
                                        contentColor = Color.DarkGray
                                    ),
                                    shape = RoundedCornerShape(6.dp),
                                    border = BorderStroke(1.dp, Color.LightGray)
                                ) {
                                    Text(stringResource(R.string.cancel))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}