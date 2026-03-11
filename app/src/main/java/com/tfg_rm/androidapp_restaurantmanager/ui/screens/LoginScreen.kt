package com.tfg_rm.androidapp_restaurantmanager.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.tfg_rm.androidapp_restaurantmanager.domain.viewmodels.AuthViewModel
import com.tfg_rm.androidapp_restaurantmanager.R
import com.tfg_rm.androidapp_restaurantmanager.ui.navigation.AppScreens

/**
 * Funcion Composable para mostrar el apartado de login de la aplicacion
 */
@Composable
fun LoginScreen(
    authViewModel: AuthViewModel = hiltViewModel(),
    loginSuccess: () -> Unit
) {
    var dni by remember { mutableStateOf("") }
    Column(
        modifier = Modifier.fillMaxSize().background(brush = Brush.linearGradient(
            colors = listOf(
                Color(0xFF0F172A),
                Color(0xFF1E293B),
                Color(0xFF0F172A)
            )
        )),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .background(
                    Color(255,255,255),
                    RoundedCornerShape(16.dp)
                )
                .padding(20.dp)
                .width(320.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            Color(0xFFF59E0B),
                            CircleShape
                        )
                        .size(50.dp, 50.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person, // o Info
                        contentDescription = "Icono usuario",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
                Text(text = "RestaurantePro", fontSize = 23.sp)
                Text(stringResource(R.string.loginscreen_subtitle), fontSize = 14.sp)
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(stringResource(R.string.loginscreen_userquest), fontSize = 14.sp)
                    TextField(
                        value = dni,
                        onValueChange = {
                            dni = it
                        },
                        placeholder = { Text(stringResource(R.string.loginscreen_placeholder)) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Button(
                    onClick = {
                        loginSuccess()
                        //authViewModel.loadRestaurants(dni, "")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF59E0B)
                    )
                ) {
                    Text(stringResource(R.string.login))
                }
                Text(
                    stringResource(R.string.problemhelp),
                    fontSize = 12.sp
                )
            }
        }
    }
}