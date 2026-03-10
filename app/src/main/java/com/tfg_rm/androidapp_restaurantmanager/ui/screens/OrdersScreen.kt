package com.tfg_rm.androidapp_restaurantmanager.ui.screens

import android.annotation.SuppressLint
import android.view.View
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tfg_rm.androidapp_restaurantmanager.R
import com.tfg_rm.androidapp_restaurantmanager.data.remote.dto.Order
import com.tfg_rm.androidapp_restaurantmanager.data.remote.dto.OrderItem
import com.tfg_rm.androidapp_restaurantmanager.ui.theme.Typography
import com.tfg_rm.androidapp_restaurantmanager.domain.viewmodels.OrdersViewModel

@Composable
fun OrdersScreen(ordersViewModel: OrdersViewModel) {
    Scaffold(
        topBar = {
            Column {
                Text(
                    text = stringResource(R.string.ordersTitle),
                    style = Typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stringResource(R.string.ordersSubtitle),
                    style = Typography.bodyMedium,
                    color = Color.Gray
                )
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
            items(ordersViewModel.ordersList) { order ->
                OrderCard(order, ordersViewModel)
            }
        }
    }
}

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
                        text = stringResource(R.string.tableLabel, order.tableId),
                        style = Typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    StatusBadge(order.statusId, viewModel)
                }
                Text(
                    text = stringResource(R.string.currencyFormat, order.total),
                    style = Typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(
                text = stringResource(R.string.timeAgo, viewModel.getMinutesAgo(order.createdAt)),
                style = Typography.bodySmall,
                color = Color.Gray,
                modifier = Modifier.padding(vertical = 4.dp)
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), thickness = 0.5.dp, color = Color.LightGray)

            order.orderItemsList.forEach { item ->
                OrderItemRow(item)
                Spacer(modifier = Modifier.height(8.dp))
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), thickness = 0.5.dp, color = Color.LightGray)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (order.statusId == 3) {
                    Button(
                        onClick = { },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C853)),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 16.dp)
                    ) {
                        Icon(painter = painterResource(R.drawable.check_circle_svgrepo_com), contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(stringResource(R.string.markDelivered), fontWeight = FontWeight.Bold)
                    }
                }
                
                IconButton(onClick = {  }) {
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

@Composable
fun OrderItemRow(item: OrderItem) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.itemQuantityFormat, item.quantity, item.dishName),
            style = Typography.bodyLarge,
            fontSize = 16.sp
        )
        if (!item.notes.isNullOrEmpty()) {
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
                    text = item.notes,
                    style = Typography.bodySmall,
                    color = Color.Gray
                )
            }
        }
    }
}


@Composable
fun StatusBadge(statusId: Int, viewModel: OrdersViewModel) {
    val colors = viewModel.getStatusColors(statusId)
    
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
                painter = if (statusId == 2) {
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
                text = stringResource(viewModel.getStatusStringRes(statusId)),
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
    val ordersViewModel : OrdersViewModel = viewModel()
    OrdersScreen(ordersViewModel)
}