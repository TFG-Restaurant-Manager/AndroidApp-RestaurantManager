package com.tfg_rm.androidapp_restaurantmanager.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.tfg_rm.androidapp_restaurantmanager.R
import com.tfg_rm.androidapp_restaurantmanager.data.remote.dto.Employees
import com.tfg_rm.androidapp_restaurantmanager.ui.navigation.AppScreens

/**
 * Funcion Composable para mostrar el apartado de login de la aplicacion
 */
@Composable
fun ProfileScreen(navController: NavHostController) {
    val empleado = Employees(
        id = 1,
        roleName = "Camarero",
        name = "Adsa",
        email = "email@email.com",
        numberPhone = "123456789",
        dni = "12345678A",
        workSchedules = listOf("De 1 a 2", "Descansa", "De 3 a 4", "De 1 a 2", "Descansa", "De 3 a 4", "Yo q se"),
    )
    Scaffold(
        topBar = { TopBar(empleado) }
    ) { paddingValues ->
        Column(modifier = Modifier
            .padding(paddingValues)
            .fillMaxWidth()){
            PersonalInformation(empleado)
            ScheduleInformation(empleado)
            Button(
                shape = RoundedCornerShape(8.dp),
                colors = ButtonColors(
                    contentColor = Color.White,
                    containerColor = Color.White,
                    disabledContainerColor = Color.White,
                    disabledContentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                border = BorderStroke(width = 1.dp, color = Color.Red),
                onClick = {navController.navigate(AppScreens.LoginScreen.route)}
            ) {
                Icon(
                    painter = painterResource(R.drawable.log_out_icon),
                    modifier = Modifier.size(16.dp),
                    contentDescription = "Log Out",
                    tint = Color.Red
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = "Cerrar sesion", color = Color.Red)
            }
        }
    }
}

@Composable
fun TopBar(empleado : Employees) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp)) {
        Text(
            text = "Mi perfil",
            style = MaterialTheme.typography.titleMedium,
            color = Color.Black,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(Modifier
                .size(48.dp)
                .background(
                    color = Color.Black,
                    shape = RoundedCornerShape(48.dp)
                )
            ) {
                Text(
                    text = empleado.name.take(2),
                    modifier = Modifier.align(alignment = Alignment.Center),
                    color = Color.White)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column() {
                Text(empleado.name)
                Text(text = empleado.name,
                        style = MaterialTheme.typography.bodySmall,)
            }
        }
    }
}

@Composable
fun PersonalInformation(empleado: Employees) {
    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp)
        ) {
            Text(
                text = "Información Personal",
                style = MaterialTheme.typography.titleMedium,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))

            // DNI
            Text(text = "DNI", fontWeight = FontWeight.Medium, color = Color.Gray)
            Text(text = empleado.dni, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(8.dp))

            // Email
            Text(text = "Email", fontWeight = FontWeight.Medium, color = Color.Gray)
            Text(text = empleado.email, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(8.dp))

            // Teléfono
            Text(text = "Teléfono", fontWeight = FontWeight.Medium, color = Color.Gray)
            Text(text = empleado.numberPhone, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
fun ScheduleInformation(empleado: Employees) {
    val days = listOf("Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabados", "Domingos")
    val pairedList = days.zip(empleado.workSchedules)

    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(
                    painter = painterResource(R.drawable.log_out_icon),
                    modifier = Modifier.size(16.dp),
                    contentDescription = "Horario Semanal",
                )
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = "Horario Semanal",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(pairedList
                ) { (days, workSchedules) ->
                    Row(modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(days)
                        Text(workSchedules)
                    }
                }
            }
        }
    }
}