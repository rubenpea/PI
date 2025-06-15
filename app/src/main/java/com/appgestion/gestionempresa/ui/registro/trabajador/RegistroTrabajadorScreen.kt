package com.appgestion.gestionempresa.ui.registro.trabajador

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.appgestion.gestionempresa.R
import com.appgestion.gestionempresa.navigation.AppScreen
import dagger.hilt.android.lifecycle.HiltViewModel

@Composable
fun RegistroTrabajadorScreen(
    navController: NavController,
    viewModel: RegistroTrabajadorViewModel = hiltViewModel()
) {
    val state      by viewModel.uiState.collectAsState()
    val success    by viewModel.registroSuccess.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    // Cuando termine el registro, abrimos el diálogo
    LaunchedEffect(success) {
        if (success) {
            showDialog = true
        }
    }

    Box(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            Modifier.fillMaxWidth(),
            verticalArrangement   = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo arriba
            Image(
                painter            = painterResource(R.drawable.logo),
                contentDescription = "Logo Gestión",
                modifier           = Modifier
                    .size(80.dp)
                    .padding(bottom = 8.dp)
            )

            Text(
                text  = "Registro Trabajador",
                style = MaterialTheme.typography.titleMedium
            )

            // Campos...
            FieldWithError(
                value       = state.email,
                onValue     = viewModel::changeEmail,
                label       = "Email",
                placeholder = "usuario@correo.com",
                errorText   = state.emailError
            )
            FieldWithError(
                value        = state.password,
                onValue      = { viewModel.changePass(it) },
                label        = "Contraseña",
                placeholder  = "••••••",
                errorText    = state.passwordError ?: state.passDiffError,
                isPassword   = true
            )
            FieldWithError(
                value        = state.passwordConfirm,
                onValue      = { viewModel.changePassConfirm(it) },
                label        = "Repetir contraseña",
                placeholder  = "••••••",
                errorText    = state.passDiffError,
                isPassword   = true
            )
            FieldWithError(
                value       = state.name,
                onValue     = viewModel::changeName,
                label       = "Nombre",
                placeholder = "Tu nombre",
                errorText   = state.nameError
            )
            FieldWithError(
                value       = state.phone,
                onValue     = viewModel::changePhone,
                label       = "Teléfono",
                placeholder = "+34 600 000 000",
                errorText   = state.phoneError
            )

            Spacer(Modifier.height(8.dp))

            Button(
                onClick  = {
                    if (viewModel.validar()) viewModel.registrarTrabajador()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Registrarse")
            }
        }
    }

    // Diálogo tras éxito de registro
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { /* no cierra tocando fuera */ },
            shape            = RoundedCornerShape(16.dp),
            icon             = {
                Icon(
                    Icons.Outlined.Description,
                    contentDescription = "CV icon",
                    modifier = Modifier.size(40.dp)
                )
            },
            title            = {
                Text(
                    text  = "¡Registro exitoso!",
                    style = MaterialTheme.typography.headlineSmall
                )
            },
            text             = {
                Text(
                    text  = "¿Quieres crear tu CV ahora?",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton    = {
                Button(
                    onClick = {
                        showDialog = false
                        viewModel.resetSuccess()
                        navController.navigate(AppScreen.CVFormScreen.route) {
                            popUpTo(AppScreen.RegistroTrabajadorScreen.route) { inclusive = true }
                        }
                    },
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text("Crear CV")
                }
            },
            dismissButton    = {
                TextButton(
                    onClick = {
                        showDialog = false
                        viewModel.resetSuccess()
                        navController.navigate(AppScreen.HomeTrabajador.route) {
                            popUpTo(AppScreen.RegistroTrabajadorScreen.route) { inclusive = true }
                        }
                    }
                ) {
                    Text("Más tarde")
                }
            }
        )
    }
}

@Composable
private fun FieldWithError(
    value: String,
    onValue: (String) -> Unit,
    label: String,
    placeholder: String,
    errorText: String?,
    isPassword: Boolean = false
) {
    Column(Modifier.fillMaxWidth()) {
        if (isPassword) {
            var visible by remember { mutableStateOf(false) }
            OutlinedTextField(
                value = value,
                onValueChange = onValue,
                label = { Text(label) },
                placeholder = { Text(placeholder) },
                visualTransformation = if (visible)
                    VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick =  { visible = !visible } ){
                        Icon(
                            imageVector = if (visible)
                                Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = if (visible)
                                "Ocultar contraseña" else "Mostrar contraseña"
                        )
                    }
                },
                isError = errorText != null,
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            OutlinedTextField(
                value = value,
                onValueChange = onValue,
                label = { Text(label) },
                placeholder = { Text(placeholder) },
                isError = errorText != null,
                modifier = Modifier.fillMaxWidth()
            )
        }

        errorText?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .padding(start = 16.dp, top = 4.dp)
                    .fillMaxWidth()
            )
        }
    }
}