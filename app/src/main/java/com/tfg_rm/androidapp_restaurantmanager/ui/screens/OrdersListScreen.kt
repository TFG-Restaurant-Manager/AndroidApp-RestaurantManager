package com.tfg_rm.androidapp_restaurantmanager.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tfg_rm.androidapp_restaurantmanager.R
import com.tfg_rm.androidapp_restaurantmanager.domain.models.Order
import com.tfg_rm.androidapp_restaurantmanager.domain.models.OrderItem
import com.tfg_rm.androidapp_restaurantmanager.domain.models.UiState
import com.tfg_rm.androidapp_restaurantmanager.domain.viewmodels.OrdersViewModel
import com.tfg_rm.androidapp_restaurantmanager.ui.theme.Typography
import java.util.Locale

/**
 * Screen that displays the list of active customer orders in the restaurant.
 * It uses [OrdersViewModel] to manage the state of the orders list. It features a
 * styled TopBar and a [LazyColumn] for efficient scrolling through order cards.
 * - **Idle/Loading:** Triggers the fetch process and shows a loading indicator.
 * - **Error:** Displays a localized error message with a manual refresh option.
 * - **Success:** Lists all current orders sorted by their table identifier.
 * @param ordersViewModel ViewModel responsible for fetching and managing order states.
 */
@Composable
fun OrdersScreen(
    ordersViewModel: OrdersViewModel = hiltViewModel()
) {
    val orderState by ordersViewModel.orders.collectAsState()

    when (orderState) {
        is UiState.Idle -> {
            ordersViewModel.getOrders()
        }

        is UiState.Loading -> LoadingScreen("")
        is UiState.Error -> {
            val error = (orderState as UiState.Error).message
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
                            onClick = { ordersViewModel.getOrders() },
                            modifier = Modifier.width(200.dp)
                        ) {
                            Text("Recargar")
                        }
                    }
                }
            }
        }

        is UiState.Success -> {
            val orders = (orderState as UiState.Success).data.sortedBy { it.tableId }
            Scaffold(
                topBar = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFF59E0B))
                            .statusBarsPadding()
                            .padding(horizontal = 16.dp)
                    ) {
                        Column {
                            Text(
                                text = stringResource(R.string.orders_title).uppercase(Locale.getDefault()),
                                style = Typography.titleLarge.copy(
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.SemiBold
                                ),
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = stringResource(R.string.orders_subtitle),
                                style = Typography.bodyMedium.copy(fontSize = 14.sp),
                                color = Color.White.copy(alpha = 0.95f)
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Divider(color = Color.White.copy(alpha = 0.18f), thickness = 1.dp)
                        }
                    }
                }
            ) { paddingValues ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(vertical = 16.dp)
                ) {
                    items(orders) { order ->
                        OrderCard(order, ordersViewModel)
                    }
                }
            }
        }

        else -> {}
    }
}

/**
 * A detailed card representing a single customer order.
 * Displays essential information including:
 * - Table number and current order status (via [StatusBadge]).
 * - Total monetary amount and elapsed time since creation.
 * - A detailed list of ordered items (via [OrderItemRow]).
 * - Action buttons for marking orders as delivered or removing them from the view.
 * @param order The [Order] domain model containing the data to display.
 * @param viewModel ViewModel used to handle status formatting and order removal logic.
 */
@Composable
fun OrderCard(order: Order, viewModel: OrdersViewModel) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = stringResource(R.string.table_label, order.tableId),
                        style = Typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    StatusBadge(
                        order.status,
                        viewModel
                    )
                }
                Text(
                    text = stringResource(R.string.currency_format, order.total),
                    style = Typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(
                text = stringResource(R.string.time_ago, viewModel.getMinutesAgo(order.createdAt)),
                style = Typography.bodySmall,
                color = Color.Gray,
                modifier = Modifier.padding(vertical = 4.dp)
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                thickness = 0.5.dp,
                color = Color.LightGray
            )

            order.orderItemsList.forEach { item ->
                OrderItemRow(
                    item,
                    order.orderItemsList.count { it.dishId == item.dishId }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                thickness = 0.5.dp,
                color = Color.LightGray
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (viewModel.getStatusStringRes(order.status) == R.string.order_statusready) {
                    Button(
                        onClick = { },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C853)),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 16.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.check_circle_svgrepo_com),
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(stringResource(R.string.mark_delivered), fontWeight = FontWeight.Bold)
                    }
                }

                IconButton(onClick = { viewModel.removeOrderById(order.id) }) {
                    Icon(
                        painter = painterResource(R.drawable.cross_svgrepo_com),
                        contentDescription = "Cancel",
                        tint = Color(0xFFD32F2F)
                    )
                }
            }
        }
    }
}

/**
 * Renders a single row for an item within an order.
 * It shows the quantity, dish name, and the calculated total price for that specific item.
 * If the item contains specific kitchen notes (e.g., "no salt"), an additional
 * row with an edit icon and the note text is displayed.
 * @param item The specific [OrderItem] to be displayed.
 * @param quantity The total count of this specific dish within the order.
 */
@Composable
fun OrderItemRow(item: OrderItem, quantity: Int) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(
                    R.string.item_quantityformat,
                    quantity,
                    item.dishName
                ),
                style = Typography.bodyLarge,
                fontSize = 16.sp
            )
            Text(
                text = "${quantity * item.price} €",
                style = Typography.bodyLarge,
                fontSize = 16.sp
            )
        }
        val notes = item.notes
        if (notes != null) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 4.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.edit_svgrepo_com),
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = notes,
                    style = Typography.bodySmall,
                    color = Color.Gray
                )
            }
        }
    }
}

/**
 * A visual indicator (badge) representing the current state of an order.
 * Dynamically adjusts its background color, text color, and icon based on the
 * status identifier (e.g., "CREATED", "COOKED"). This helps staff quickly
 * distinguish between pending, ready, or delivered items at a glance.
 * @param status The technical status string from the backend.
 * @param viewModel ViewModel used to map the status to specific colors and localized strings.
 */
@Composable
fun StatusBadge(status: String, viewModel: OrdersViewModel) {
    val colors = viewModel.getStatusColors(status)

    val backgroundColor = colors.first
    val contentColor = colors.second


    Surface(
        color = backgroundColor,
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
        ) {
            Icon(
                painter = if (viewModel.getStatusStringRes(status) == R.string.order_statuscreated) {
                    painterResource(id = R.drawable.time_svgrepo_com) // Tu archivo local
                } else {
                    painterResource(id = R.drawable.check_circle_svgrepo_com)
                },
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(14.dp)
            )

            Spacer(modifier = Modifier.width(4.dp))

            Text(
                text = stringResource(viewModel.getStatusStringRes(status)),
                color = contentColor,
                style = Typography.labelSmall,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OrdersScreenPreview() {
    val ordersViewModel: OrdersViewModel = viewModel()
    OrdersScreen(ordersViewModel)
}