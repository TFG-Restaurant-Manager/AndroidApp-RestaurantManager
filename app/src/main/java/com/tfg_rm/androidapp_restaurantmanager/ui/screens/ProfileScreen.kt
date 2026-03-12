package com.tfg_rm.androidapp_restaurantmanager.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tfg_rm.androidapp_restaurantmanager.R
import com.tfg_rm.androidapp_restaurantmanager.data.remote.dto.EmployeesDto
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth

@Preview
@Composable
fun PreviewProfileScreen() {
    ScheduleInformation(
        empleado = EmployeesDto(
            id = 1,
            roleName = "Camarero",
            name = "Adsa",
            email = "email@email.com",
            numberPhone = "123456789",
            dni = "12345678A",
            workSchedules = listOf(
                LocalDateTime.now().minusDays(1) to LocalDateTime.now().minusDays(1).plusHours(8),
                LocalDateTime.now().minusDays(2) to LocalDateTime.now().minusDays(2).plusHours(8),
                LocalDateTime.now().minusDays(3) to LocalDateTime.now().minusDays(3).plusHours(8),
                LocalDateTime.now().minusDays(4) to LocalDateTime.now().minusDays(4).plusHours(8),
                LocalDateTime.now().minusDays(5) to LocalDateTime.now().minusDays(5).plusHours(8)
            )
        )
    )
}

/**
 * Funcion Composable para mostrar el apartado de login de la aplicacion
 */
@Composable
fun ProfileScreen(
    BackToLogin: () -> Unit
) {
    val empleado = EmployeesDto(
        id = 1,
        roleName = "Camarero",
        name = "Adsa",
        email = "email@email.com",
        numberPhone = "123456789",
        dni = "12345678A",
        workSchedules = listOf(
        )
    )
    Scaffold(
        topBar = { TopBar(empleado) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
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
                onClick = { BackToLogin() }
            ) {
                Icon(
                    painter = painterResource(R.drawable.log_out_icon),
                    modifier = Modifier.size(16.dp),
                    contentDescription = stringResource(R.string.logout),
                    tint = Color.Red
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = stringResource(R.string.logout), color = Color.Red)
            }
        }
    }
}

@Composable
fun TopBar(empleado: EmployeesDto) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = stringResource(R.string.profilescreen_titleself),
            style = MaterialTheme.typography.titleMedium,
            color = Color.Black,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                Modifier
                    .size(48.dp)
                    .background(
                        color = Color.Black,
                        shape = RoundedCornerShape(48.dp)
                    )
            ) {
                Text(
                    text = empleado.name.take(2),
                    modifier = Modifier.align(alignment = Alignment.Center),
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column() {
                Text(empleado.name)
                Text(
                    text = empleado.name,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
    }
}

@Composable
fun PersonalInformation(empleado: EmployeesDto) {
    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.profilescreen_personalinformation),
                style = MaterialTheme.typography.titleMedium,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))

            // DNI
            Text(
                text = stringResource(R.string.dni),
                fontWeight = FontWeight.Medium,
                color = Color.Gray
            )
            Text(text = empleado.dni, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(8.dp))

            // Email
            Text(text = "Email", fontWeight = FontWeight.Medium, color = Color.Gray)
            Text(text = empleado.email, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(8.dp))

            // Teléfono
            Text(
                text = stringResource(R.string.telephone),
                fontWeight = FontWeight.Medium,
                color = Color.Gray
            )
            Text(text = empleado.numberPhone, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
fun ScheduleInformation(empleado: EmployeesDto) {
    val months = stringArrayResource(R.array.months_year)
    val actualDate = LocalDate.now();
    var daySelected by remember { mutableStateOf<LocalDate?>(null) }
    var monthSelected by remember { mutableStateOf(actualDate.monthValue) }
    var yearSelected by remember { mutableStateOf(actualDate.year) }

    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.log_out_icon),
                        modifier = Modifier.size(16.dp),
                        contentDescription = stringResource(R.string.profilescreen_personalschedule),
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = stringResource(R.string.profilescreen_personalschedule),
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                }
                IconButton(
                    onClick = {
                        monthSelected = LocalDate.now().monthValue
                        yearSelected = LocalDate.now().year
                    }
                ) {
                    Icon(Icons.Default.Refresh, null)
                }
            }
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        if (monthSelected == 1) {
                            monthSelected = 12
                            yearSelected--
                        } else monthSelected--
                    }
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBackIos, null)
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        months[monthSelected - 1],
                        fontSize = MaterialTheme.typography.titleMedium.fontSize
                    )
                    Text(
                        yearSelected.toString(),
                        fontSize = MaterialTheme.typography.titleMedium.fontSize
                    )
                }
                IconButton(
                    onClick = {
                        if (monthSelected == 12) {
                            monthSelected = 1
                            yearSelected++
                        } else monthSelected++
                    }
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowForwardIos, null)
                }

            }
            CalendarioMes(
                monthSelected,
                yearSelected,
                contarDias(empleado.workSchedules),
                mirarDia = { daySelected = it }
            )

            Spacer(Modifier.height(10.dp))

            daySelected?.let {
                HorarioDia(
                    dia = it.dayOfMonth,
                    mes = it.monthValue - 1,
                    año = it.year,
                    empleado.workSchedules.filter { (inicio, fin) ->
                        // Si el rango incluye al día concreto
                        inicio.toLocalDate() == daySelected ||
                                fin.toLocalDate() == daySelected
                    }
                )
            }
        }
    }
}

@Composable
fun CalendarioMes(
    month: Int,
    year: Int,
    horario: Map<LocalDate, Int>,
    mirarDia: (LocalDate) -> Unit
) {

    val nombresDiasSemana = stringArrayResource(R.array.days_week_short)

    val yearMonth = YearMonth.of(year, month)
    val longitudMes = yearMonth.lengthOfMonth()

    val primerDiaSemana = LocalDate.of(year, month, 1).dayOfWeek.value - 1

    // lista con huecos iniciales + días del mes
    val dias = (MutableList<Int?>(primerDiaSemana) { null } +
            (1..longitudMes).map { it }).toMutableList()

    // Rellenamos con null hasta que sea múltiplo de 7
    while (dias.size % 7 != 0) {
        dias.add(null)
    }

    Column {
        // CABECERA DIAS
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            nombresDiasSemana.forEach { nombre ->
                Text(
                    nombre,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
            }
        }

        // CALENDARIO
        dias.chunked(7).forEach { semana ->

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {

                semana.forEach { dia ->

                    Box(
                        modifier = if (dia != null)
                            if (horario.contains(LocalDate.of(year, month, dia)))
                                Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .padding(2.dp)
                                    .background(Color(0xFFDFF6E3), RoundedCornerShape(6.dp))
                                    .clickable(onClick = {
                                        mirarDia(
                                            LocalDate.of(
                                                year,
                                                month,
                                                dia
                                            )
                                        )
                                    })
                                    .border(
                                        width = if (dia == LocalDate.now().dayOfMonth) 1.dp else 0.dp,
                                        color = if ((dia == LocalDate.now().dayOfMonth)) Color(
                                            0xFF4CAF50
                                        ) else Color.Transparent,
                                        shape = RoundedCornerShape(6.dp)
                                    )
                            else
                                Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .padding(2.dp)
                                    .background(Color(0xFFF5F5F5), RoundedCornerShape(6.dp))
                                    .clickable(onClick = {
                                        mirarDia(
                                            LocalDate.of(
                                                year,
                                                month,
                                                dia
                                            )
                                        )
                                    })
                                    .border(
                                        width = if (dia == LocalDate.now().dayOfMonth) 1.dp else 0.dp,
                                        color = if ((dia == LocalDate.now().dayOfMonth)) Color(
                                            0xFF9E9E9E
                                        ) else Color.Transparent,
                                        shape = RoundedCornerShape(6.dp)
                                    )
                        else Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .padding(4.dp),
                        contentAlignment = Alignment.Center
                    ) {

                        if (dia != null) {
                            Text(
                                dia.toString(),
                                color =
                                    if (horario.contains(LocalDate.of(year, month, dia)))
                                        Color.Black
                                    else
                                        Color.Gray
                            )
                        }

                    }
                }
            }
        }
    }
}

fun contarDias(horarios: List<Pair<LocalDateTime, LocalDateTime>>): Map<LocalDate, Int> {
    // Creamos una lista con todos los días de cada rango
    val dias = horarios.flatMap { (inicio, fin) ->
        // Si el rango es en un solo día, solo devolvemos ese día
        if (inicio.toLocalDate() == fin.toLocalDate()) {
            listOf(inicio.toLocalDate())
        } else {
            // Si abarca varios días, generamos la secuencia de días
            generateSequence(inicio.toLocalDate()) { dia ->
                val siguiente = dia.plusDays(1)
                if (siguiente <= fin.toLocalDate()) siguiente else null
            }.toList()
        }
    }

    // Contamos las veces que aparece cada día
    return dias.groupingBy { it }.eachCount()
}

@Composable
fun HorarioDia(dia: Int, mes: Int, año: Int, horarios: List<Pair<LocalDateTime, LocalDateTime>>) {
    val meses = stringArrayResource(R.array.months_year)
    val mes = meses[mes]
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF0F0F0)
        ),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text("$dia ${stringResource(R.string.of)} $mes ${stringResource(R.string.of)} $año")
            if (horarios.isEmpty()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.AccessTime, null, tint = Color(0xFF9EE6B8))
                    Text(stringResource(R.string.profilescreen_dayoff), color = Color(0xFF9EE6B8))
                }
            } else {
                horarios.forEach { horario ->
                    val inicio = horario.component1()

                    val turno = when (inicio.hour) {
                        in 6..13 -> stringResource(R.string.profilescreen_morningshift)
                        in 14..21 -> stringResource(R.string.profilescreen_afternoonshift)
                        else -> stringResource(R.string.profilescreen_nightshift)
                    }
                    Row {
                        Text(
                            text = "${turno}: "
                        )
                        Text(
                            "${"%02d".format(horario.component1().hour)}:${"%02d".format(horario.component1().minute)} - " +
                                    "${"%02d".format(horario.component2().hour)}:${
                                        "%02d".format(
                                            horario.component2().minute
                                        )
                                    }"
                        )
                    }
                }
            }
        }
    }
}