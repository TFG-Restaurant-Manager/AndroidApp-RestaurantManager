package com.tfg_rm.androidapp_restaurantmanager.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.tfg_rm.androidapp_restaurantmanager.R
import com.tfg_rm.androidapp_restaurantmanager.domain.viewmodels.AuthState
import com.tfg_rm.androidapp_restaurantmanager.domain.viewmodels.AuthViewModel

/**
 * Funcion Composable para mostrar el apartado de login de la aplicacion
 */
@Composable
fun LoginScreen(
    authViewModel: AuthViewModel = hiltViewModel(),
    loginSuccess: () -> Unit
) {
    LaunchedEffect(Unit) {
        authViewModel.resetState()
    }
    var code by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val authState by authViewModel.authState.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF0F172A),
                        Color(0xFF1E293B),
                        Color(0xFF0F172A)
                    )
                )
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .background(
                    Color(255, 255, 255),
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
                    PersOutlinedTextField(
                        valor = code,
                        label = stringResource(R.string.loginscreen_userquest),
                        placeholder = stringResource(R.string.loginscreen_placeholder),
                        onValueChange = {
                            code = it
                        }
                    )
                    PersOutlinedTextField(
                        valor = password,
                        label = stringResource(R.string.loginscreen_password_label),
                        placeholder = stringResource(R.string.loginscreen_password_placeholder),
                        onValueChange = { password = it },
                        password = true
                    )
                }
                val context = LocalContext.current
                LaunchedEffect(authState) {
                    if (authState is AuthState.Error) {
                        Toast.makeText(
                            context,
                            (authState as AuthState.Error).msg,
                            Toast.LENGTH_SHORT
                        ).show()

                        authViewModel.resetState()
                    }
                }
                when (authState) {
                    is AuthState.Error -> {
                    }

                    is AuthState.Idle -> {
                        Button(
                            onClick = {
                                authViewModel.login(
                                    code,
                                    password
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFF59E0B)
                            )
                        ) {
                            Text(stringResource(R.string.login))
                        }
                    }

                    is AuthState.Loading -> {
                        Text(stringResource(R.string.loading_generic))
                    }

                    is AuthState.Success -> {
                        loginSuccess()
                    }
                }
                Text(
                    stringResource(R.string.problemhelp),
                    fontSize = 12.sp
                )
            }
        }
    }
}

/**
 * Campo de texto personalizado con estilo visual propio.
 *
 * Permite:
 * - Campo normal.
 * - Campo de email (configura teclado tipo Email).
 * - Campo de contraseña con opción de mostrar/ocultar texto.
 *
 * Utiliza [BasicTextField] para mayor personalización visual.
 *
 * Características:
 * - Borde dinámico en caso de error.
 * - Placeholder personalizado.
 * - Icono para mostrar/ocultar contraseña.
 * - Configuración automática del teclado según tipo.
 *
 * @param valor Texto actual del campo.
 * @param label Texto descriptivo mostrado encima del campo.
 * @param placeholder Texto mostrado cuando el campo está vacío.
 * @param onValueChange Callback que se ejecuta cuando cambia el texto.
 * @param enabled Indica si el campo está habilitado.
 * @param isError Indica si el campo presenta un estado de error visual.
 * @param password Indica si el campo es de tipo contraseña.
 * @param email Indica si el campo es de tipo email.
 */
@Composable
fun PersOutlinedTextField(
    valor: String,
    label: String,
    placeholder: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean = true,
    isError: Boolean = false,
    password: Boolean = false,
    email: Boolean = false
) {
    var contraseñaVisible by remember { mutableStateOf(false) }
    val colorBorde = when {
        isError -> Color.Red
        else -> Color.Black
    }
    // Define el tipo de teclado según si es email o contraseña
    val opcionesTeclado = when {
        password -> KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        )

        email -> KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        )

        else -> KeyboardOptions.Default
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(label, modifier = Modifier.align(Alignment.Start))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(36.dp) // h-9
                .background(
                    color = Color.White, // bg-input-background
                    shape = RoundedCornerShape(6.dp) // rounded-md
                )
                .border(
                    width = 1.dp,
                    color = colorBorde,
                    shape = RoundedCornerShape(6.dp)
                )
                .padding(horizontal = 12.dp),
            contentAlignment = Alignment.CenterStart,
        ) {
            BasicTextField(
                value = valor,
                onValueChange = onValueChange,
                enabled = enabled,
                singleLine = true,
                keyboardOptions = opcionesTeclado,
                textStyle = TextStyle(
                    fontSize = 16.sp,
                    color = Color.Black
                ),
                visualTransformation = if (password && !contraseñaVisible) PasswordVisualTransformation() else VisualTransformation.None,
                modifier = Modifier.fillMaxWidth(),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        if (valor.isEmpty()) {
                            Text(
                                text = placeholder,
                                fontSize = 16.sp,
                                color = Color.Gray
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(modifier = Modifier.weight(1f)) {
                                innerTextField()
                            }
                            if (password) {
                                Icon(
                                    imageVector = if (contraseñaVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                    contentDescription = if (contraseñaVisible) "Ocultar contraseña" else "Mostrar contraseña",
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clickable { contraseñaVisible = !contraseñaVisible }
                                )
                            }
                        }
                    }
                }
            )
        }
    }
}