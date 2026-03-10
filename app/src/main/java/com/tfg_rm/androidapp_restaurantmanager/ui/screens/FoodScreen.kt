package com.tfg_rm.androidapp_restaurantmanager.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.tfg_rm.androidapp_restaurantmanager.controller.FoodController
import com.tfg_rm.androidapp_restaurantmanager.data.remote.dto.Dishes
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
    val dishes : List<Dishes> = controller.getDishes()
    val dishesCategories: List<String> = controller.getDishesCategories(dishes)
    var selectedCategory by remember { mutableStateOf(dishesCategories[0]) }
    val order = remember { mutableStateOf(controller.getOrder(tableId)) }

    Scaffold(
        topBar = {
            TopTableBar("Mesa $tableId", onBack = { controller.backToTables(navController) })
        },
        bottomBar = {
            if (controller.getOrderDishesQuantity(order) > 0) {
                BottomTableBar(
                    getOrderDishesQuantity = { controller.getOrderDishesQuantity(order) },
                    getOrderTotalAmount = { controller.getOrderTotalAmount(order) }
                )
            }
        }
    ) {paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
        ) {
            var searchedDish by remember { mutableStateOf("") }

            OutlinedTextField(
                value = searchedDish,
                onValueChange = { searchedDish = it },
                placeholder = { Text("Buscar producto...") },
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
            CategorySelector(dishesCategories,
                selectedCategory,
                onCategorySelected = {selectedCategory = it}
            )

            DishesList(controller.filterDishes(dishes, searchedDish, selectedCategory),
                onAddDish = {dishId -> controller.addDishToOrder(order, dishId)},
                onPlusDish = {dishId -> controller.plusDishOnOrder(order, dishId)},
                onMinusDish = {dishId -> controller.minusDishOnOrder(order, dishId)},
                isDishInOrder = {dishId -> controller.isDishInOrder(order, dishId)},
                getDishQuantityInOrder = {dishId-> controller.getDishQuantityInOrder(order, dishId)},
                getNotes = {dishId -> controller.getNotes(dishId, order)},
                isNoteEmpty = {dishId -> controller.isNoteEmpty(dishId, order)},
                onUpdateNote = {dishId, newNote -> controller.updateNotes(dishId,newNote, order)})
        }
    }
}

@Composable
fun BottomTableBar (
    getOrderDishesQuantity: () -> Int,
    getOrderTotalAmount: () -> Double
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RectangleShape,
        border = BorderStroke(1.dp, Color.LightGray)
    ) {
        Column (
            modifier = Modifier.padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("${getOrderDishesQuantity()} productos")
                Text("${getOrderTotalAmount()} €")
            }
            Button(
                onClick = {},
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF10B981),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(6.dp)
            ) {
                Text("Enviar pedido a cocina")
            }
        }
    }
}

@Composable
fun TopTableBar (tableName: String,onBack: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        IconButton (
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
                border = if (selectedCategory != category) BorderStroke(1.dp, Color.LightGray) else null,
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

@Composable
fun DishesList (
    dishes: List<Dishes>,
    onAddDish: (dish: Dishes) -> Unit,
    onPlusDish: (dish: Dishes) -> Unit,
    onMinusDish: (dish: Dishes) -> Unit,
    isDishInOrder: (dishId: Int) -> Boolean,
    getDishQuantityInOrder: (dishId: Int) -> Int,
    getNotes: (dishId: Int) -> String,
    isNoteEmpty: (dishId: Int) -> Boolean,
    onUpdateNote: (dishId: Int, newNote: String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth()
    ) {
        items( dishes ) { dish ->
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
                Column (
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
                        Column(

                        ) {
                            Text(dish.name)
                            Text(String.format(Locale.getDefault(), "%.2f €", dish.price),
                                color = Color(0xFFF59E0B))

                        }
                        if (isDishInOrder(dish.id)) {
                            Row (
                                verticalAlignment = Alignment.CenterVertically
                            ){
                                IconButton(
                                    onClick = {
                                        onMinusDish(dish)
                                    }
                                ) {
                                    Text("-")
                                }
                                Text("${getDishQuantityInOrder(dish.id)}")
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
                                Text("Agregar")
                            }
                        }
                    }
                    if (isDishInOrder(dish.id)) {
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
                                Text("Agregar observaciones",
                                    color = Color.DarkGray)
                            }
                        } else {
                            BasicTextField(
                                value = getNotes(dish.id),
                                onValueChange = { newValue ->
                                    onUpdateNote(dish.id, newValue)
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
                                        if (isNoteEmpty(dish.id)) {
                                            Text(
                                                text = "Observaciones (sin cebolla, sin sal, alergias, etc.)",
                                                color = Color.Gray
                                            )
                                        }
                                        innerTextField()
                                    }
                                }
                            )

                            Row (
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Button(
                                    onClick = {
                                        notesOpen.value = false
                                    },
                                    modifier = Modifier.weight(0.7f),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFF10B981),
                                        contentColor = Color.White
                                    ),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text("Guardar")
                                }

                                Button(
                                    onClick = {
                                        notesOpen.value = false
                                        onUpdateNote(dish.id, "")
                                    },
                                    modifier = Modifier.weight(0.3f),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color.White,
                                        contentColor = Color.DarkGray
                                    ),
                                    shape = RoundedCornerShape(6.dp),
                                    border = BorderStroke(1.dp, Color.LightGray)
                                ) {
                                    Text("Cancelar")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}