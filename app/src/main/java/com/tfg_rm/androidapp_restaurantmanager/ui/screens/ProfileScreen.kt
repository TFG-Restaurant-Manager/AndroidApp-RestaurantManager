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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
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
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.tfg_rm.androidapp_restaurantmanager.R
import com.tfg_rm.androidapp_restaurantmanager.domain.models.Employee
import com.tfg_rm.androidapp_restaurantmanager.domain.models.UiState
import com.tfg_rm.androidapp_restaurantmanager.domain.viewmodels.AuthViewModel
import com.tfg_rm.androidapp_restaurantmanager.domain.viewmodels.EmployeeViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import java.util.Locale

@Preview
@Composable
fun PreviewProfileScreen() {
    ScheduleInformation(
        empleado = Employee(
            roleName = "Camarero",
            name = "Adsa",
            email = "email@email.com",
            phone = "123456789",
            schedules = listOf(
                LocalDateTime.now().minusDays(1) to LocalDateTime.now().minusDays(1).plusHours(8),
                LocalDateTime.now().minusDays(2) to LocalDateTime.now().minusDays(2).plusHours(8),
                LocalDateTime.now().minusDays(3) to LocalDateTime.now().minusDays(3).plusHours(8),
                LocalDateTime.now().minusDays(4) to LocalDateTime.now().minusDays(4).plusHours(8),
                LocalDateTime.now().minusDays(5) to LocalDateTime.now().minusDays(5).plusHours(8)
            )
        )
    )
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen(
        viewModel = hiltViewModel(),
        authViewModel = hiltViewModel()
    )
}

/**
 * Profile screen that displays detailed employee information and their work schedule.
 * * Manages the different interface states ([UiState]):
 * - **Idle/Loading:** Displays a loading screen while fetching data.
 * - **Error:** Allows the user to retry the data fetch or log out of the session.
 * - **Success:** Renders personal information, the shift calendar, and the logout option.
 * @param viewModel ViewModel responsible for employee data logic.
 * @param authViewModel ViewModel responsible for session and authentication management.
 */
@Composable
fun ProfileScreen(
    viewModel: EmployeeViewModel,
    authViewModel: AuthViewModel
) {
    val employeeState by viewModel.employeeState.collectAsState()
    when (employeeState) {
        is UiState.Idle -> {
            viewModel.getEmployeeData()
        }

        is UiState.Error -> {
            val error = (employeeState as UiState.Error).message
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
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(stringResource(error))
                        Button(
                            onClick = { viewModel.getEmployeeData() },
                            modifier = Modifier.width(200.dp)
                        ) {
                            Text("Recargar")
                        }
                        Button(
                            onClick = {
                                authViewModel.logout()
                            },
                            modifier = Modifier.width(200.dp)
                        ) {
                            Text("Cerrar sesion")
                        }
                    }
                }
            }
        }

        is UiState.Loading -> LoadingScreen(stringResource(R.string.profilescreen_loading))

        is UiState.Success -> {
            val empleado = (employeeState as UiState.Success<Employee>).data
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
                        onClick = {
                            authViewModel.logout()
                        }
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

        else -> {}
    }
}

/**
 * Custom top bar that displays a generated avatar with the employee's initials,
 * their full name, and their specific role within the restaurant.
 * @param empleado The employee data object to be displayed.
 */
@Composable
fun TopBar(empleado: Employee) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF59E0B))
            .statusBarsPadding()
            .padding(horizontal = 16.dp)
    ) {
        Column {
            Text(
                text = stringResource(R.string.profilescreen_titleself).uppercase(Locale.getDefault()),
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp
                ),
                color = Color.White
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
                            color = Color.White,
                            shape = RoundedCornerShape(48.dp)
                        )
                ) {
                    Text(
                        text = empleado.name.take(2).uppercase(Locale.getDefault()),
                        modifier = Modifier.align(alignment = Alignment.Center),
                        color = Color(0xFFF59E0B),
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column() {
                    Text(empleado.name, color = Color.White)
                    Text(
                        text = empleado.roleName,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.95f)
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Divider(color = Color.White.copy(alpha = 0.18f), thickness = 1.dp)
        }
    }
}

/**
 * Information card that displays the employee's contact details.
 * @param empleado Object containing the worker's email and phone number.
 */
@Composable
fun PersonalInformation(empleado: Employee) {
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
            Text(text = empleado.phone, fontWeight = FontWeight.SemiBold)
        }
    }
}

/**
 * Interactive section that manages the employee's work schedule.
 * Allows navigation between months, resetting to the current date, and selecting
 * specific days to consult assigned shifts via the [HorarioDia] component.
 * @param empleado Object containing the list of time ranges ([Employee.schedules]).
 */
@Composable
fun ScheduleInformation(empleado: Employee) {
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
                contarDias(empleado.schedules),
                mirarDia = { daySelected = it },
                daySelected
            )

            Spacer(Modifier.height(10.dp))

            daySelected?.let {
                HorarioDia(
                    dia = it.dayOfMonth,
                    mes = it.monthValue - 1,
                    año = it.year,
                    empleado.schedules.filter { (inicio, fin) ->
                        // Si el rango incluye al día concreto
                        inicio.toLocalDate() == daySelected ||
                                fin.toLocalDate() == daySelected
                    }
                )
            }
        }
    }
}

/**
 * Generates a calendar grid for a specific month and year.
 * Days with assigned shifts are visually highlighted. It supports day selection
 * and highlights the current date (today) with a distinct border.
 * @param month Month to display (1-12).
 * @param year Year to display.
 * @param horario Map containing the days with shifts and the count of shifts per day.
 * @param mirarDia Callback executed when a day is clicked.
 * @param diaSeleccionado The date currently marked/selected by the user.
 */
@Composable
fun CalendarioMes(
    month: Int,
    year: Int,
    horario: Map<LocalDate, Int>,
    mirarDia: (LocalDate) -> Unit,
    diaSeleccionado: LocalDate?
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
                                    .background(
                                        if ((diaSeleccionado == LocalDate.of(
                                                year,
                                                month,
                                                dia
                                            )) ?: false
                                        ) Color(0xFFC8EED0)
                                        else Color(0xFFDFF6E3), RoundedCornerShape(6.dp)
                                    )
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
                                        width = if (LocalDate.of(
                                                year,
                                                month,
                                                dia
                                            ) == LocalDate.now()
                                        ) 1.dp else 0.dp,
                                        color = if (LocalDate.of(
                                                year,
                                                month,
                                                dia
                                            ) == LocalDate.now()
                                        ) Color(
                                            0xFF4CAF50
                                        ) else Color.Transparent,
                                        shape = RoundedCornerShape(6.dp)
                                    )
                            else
                                Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .padding(2.dp)
                                    .background(
                                        if ((diaSeleccionado == LocalDate.of(
                                                year,
                                                month,
                                                dia
                                            )) ?: false
                                        ) Color(0xFFE8E8E8)
                                        else Color(0xFFF5F5F5), RoundedCornerShape(6.dp)
                                    )
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
                                        width = if (LocalDate.of(
                                                year,
                                                month,
                                                dia
                                            ) == LocalDate.now()
                                        ) 1.dp else 0.dp,
                                        color = if (LocalDate.of(
                                                year,
                                                month,
                                                dia
                                            ) == LocalDate.now()
                                        ) Color(
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

/**
 * Processes the list of shifts (start and end pairs) to identify which calendar days
 * have work activity.
 * @param horarios List of [LocalDateTime] pairs representing clock-in and clock-out times.
 * @return A map where the key is the [LocalDate] and the value is the number of shifts on that day.
 */
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

/**
 * Detail card that shows the specific shifts for a selected day.
 * Classifies shifts as "Morning", "Afternoon", or "Night" based on the start time
 * and shows the exact time range. If no shifts exist, it indicates a day off.
 * @param dia Day of the month.
 * @param mes Month index (0-11).
 * @param año Corresponding year.
 * @param horarios Filtered list of shifts occurring on the specified date.
 */
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