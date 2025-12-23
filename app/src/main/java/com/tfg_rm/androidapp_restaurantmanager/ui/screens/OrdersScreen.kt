package com.tfg_rm.androidapp_restaurantmanager.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.tfg_rm.androidapp_restaurantmanager.R
import com.tfg_rm.androidapp_restaurantmanager.ui.theme.Typography
import com.tfg_rm.androidapp_restaurantmanager.viewmodels.OrdersViewModel

@Composable
fun TopBar() {
    Column() {
        Text(stringResource(R.string.ordersTitle), style = Typography.titleMedium)
        Text(stringResource(R.string.ordersSubtitle))
    }
}

@Composable
fun OrdersScreen(orderViewModel: OrdersViewModel) {
    Scaffold(topBar = { TopBar() }) { paddingValues ->
        LazyColumn (modifier = Modifier.padding(paddingValues)) {
            items(items = orderViewModel.ordersList) {
                Row() {
                    Text(text = "${it.tableId}")
                    Text(stringResource(orderViewModel.getStatus(it)))
                }

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OrdersScreenPreview() {
    OrdersScreen(OrdersViewModel())
}