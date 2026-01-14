package com.tfg_rm.androidapp_restaurantmanager.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.tfg_rm.androidapp_restaurantmanager.controller.FoodController
import com.tfg_rm.androidapp_restaurantmanager.data.models.Dishes
import com.tfg_rm.androidapp_restaurantmanager.data.models.Orders
import com.tfg_rm.androidapp_restaurantmanager.data.models.Tables
import java.time.LocalDate
import java.util.Locale
@Preview(showBackground = true)
@Composable
fun FoodScreenPreview() {
    FoodScreen(
        tableId = 1,
        navController = rememberNavController()
    )
}
@Composable
fun FoodScreen (
    tableId: Int,
    navController: NavHostController,
    controller: FoodController = FoodController()
) {
    val dishesExamples : List<Dishes> = listOf(
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
    val dishesCategories = dishesExamples.map { it.category }
        .distinct().let { listOf("Todo") + it }
    var selectedCategory by remember { mutableStateOf(dishesCategories[0]) }
    val orderExample = remember { mutableStateOf(
        Orders(
            1,
            tableId,
            "Creado",
            0.toDouble(),
            LocalDate.now(),
            mutableListOf()
        ))
    }
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        TopTableBar("Mesa $tableId", navController)

        var searchedDishe by remember { mutableStateOf("") }

        OutlinedTextField(
            value = searchedDishe,
            onValueChange = { searchedDishe = it },
            placeholder = { Text("Buscar producto...") },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = null)
            },
            singleLine = true,
            shape = RoundedCornerShape(50),
            modifier = Modifier.padding(horizontal = 10.dp).fillMaxWidth()
        )
        Spacer(Modifier.padding(5.dp))
        CategorySelector(dishesCategories,
            selectedCategory,
            onCategorySelected = {selectedCategory = it}
            )

        DishesList(dishesExamples,
            searchedDishe,
            selectedCategory,
            controller,
            orderExample)
    }
}

@Composable
fun TopTableBar (tableName: String, navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
    ) {
        IconButton (
            onClick = { navController.popBackStack() },
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back to Tables",
                tint = Color.Black,
                modifier = Modifier.size(14.dp)
            )
        }
        Text(tableName,
            modifier = Modifier.align(Alignment.Center))
    }
}

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
        modifier = Modifier.padding(vertical = 7.dp).fillMaxWidth(),
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
                border = if (selectedCategory != category) BorderStroke(1.dp, Color.LightGray) else null
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

@Composable
fun DishesList (
    dishes: List<Dishes>,
    searchedDishe: String,
    selectedCategory: String,
    controller: FoodController,
    order: MutableState<Orders>
) {
    val filteredDishes = controller.filterDishes(dishes, searchedDishe, selectedCategory)

    LazyColumn(
        modifier = Modifier.fillMaxWidth()
    ) {
        items( filteredDishes ) { dish ->
            Card(
                modifier = Modifier.padding(12.dp).fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                border = BorderStroke(1.dp, Color.LightGray)
            ) {
                Column (
                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(

                        ) {
                            Text(dish.name)
                            Text(String.format(Locale.getDefault(), "%.2f €", dish.price),
                                color = Color(0xFFF59E0B))

                        }
                        if (order.value.orderDishes.map { it.dishId }
                            .contains(dish.id)
                            && order.value.orderDishes.first {
                            it.dishId == dish.id
                            }.quantity > 0) {
                            Row (
                                verticalAlignment = Alignment.CenterVertically
                            ){
                                IconButton(
                                    onClick = {
                                        controller.minusDishOnOrder(order, dish)
                                    }
                                ) {
                                    Text("-")
                                }
                                Text("${order.value.orderDishes[dish.id]}")
                                IconButton(
                                    onClick = {
                                        controller.plusDishOnOrder(order, dish)
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
                            Button(
                                onClick = {
                                    controller.addDishToOrder(order, dish)
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
                                Text("Agregar")
                            }
                        }
                    }
                    if (order.value.orderDishes.map { it.dishId }
                            .contains(dish.id)
                                && order.value.orderDishes.first {
                            it.dishId == dish.id
                        }.quantity > 0) {
                        HorizontalDivider(
                            thickness = 1.dp,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White
                            ),
                            shape = RoundedCornerShape(6.dp),
                            border = BorderStroke(1.dp, Color.LightGray),
                            onClick = {
                                //Abrir notas del pedido
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add product ${dish.name} to the order",
                                tint = Color.DarkGray,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Agregar observaciones",
                                color = Color.DarkGray)
                        }
                    }
                }
            }
        }
    }
}