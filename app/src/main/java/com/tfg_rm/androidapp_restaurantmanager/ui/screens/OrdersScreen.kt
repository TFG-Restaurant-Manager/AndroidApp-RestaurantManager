package com.tfg_rm.androidapp_restaurantmanager.ui.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
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
        Text(stringResource(R.string.OrdersTitle), style = Typography.titleMedium)
        Text(stringResource(R.string.OrdersSubtitle))
    }
}

@Composable
fun OrdersScreen(orderViewModel: OrdersViewModel) {
    Scaffold(topBar = { TopBar() }) {
        LazyColumn {
            items(items = orderViewModel.ordersList) {
                Text(text = "${it.tableId}")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OrdersScreenPreview() {
    OrdersScreen(OrdersViewModel())
}