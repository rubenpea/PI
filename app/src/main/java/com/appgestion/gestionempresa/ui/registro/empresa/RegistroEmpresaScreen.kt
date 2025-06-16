package com.appgestion.gestionempresa.ui.registro.empresa

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.appgestion.gestionempresa.R
import com.appgestion.gestionempresa.navigation.AppScreen


@Composable
fun RegistroEmpresaScreen(
    navController: NavController,
    viewModel: RegistroEmpresaViewModel = hiltViewModel()
) {
    val state   by viewModel.uiState.collectAsState()
    val success by viewModel.registerUser.collectAsState()

    LaunchedEffect(success) {
        if (success) {
            navController.navigate(AppScreen.SetupEmpresaScreen.route) {
                popUpTo(AppScreen.RegistroEmpresaScreen.route) { inclusive = true }
            }
        }
    }

    Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter            = painterResource(R.drawable.logo),
                contentDescription = "Logo Gestión",
                modifier           = Modifier
                    .size(80.dp)
                    .padding(bottom = 8.dp)
            )

            Text(
                text = "Registro Empresa",
                style = MaterialTheme.typography.titleMedium
            )

            // --- CAMPOS ---
            FieldWithError(
                value       = state.email,
                onValue     = viewModel::changeEmail,
                label       = "Email",
                placeholder = "empresa@correo.com",
                errorText   = state.emailError
            )

            FieldWithError(
                value              = state.password,
                onValue            = {
                    viewModel.changePass(it)
                },
                label              = "Contraseña",
                placeholder        = "••••••",
                errorText          = state.passwordError ?: state.passDiffError,
                isPassword         = true
            )

            FieldWithError(
                value              = state.passwordConfirm,
                onValue            = {
                    viewModel.changePassConfirm(it)

                },
                label              = "Repetir contraseña",
                placeholder        = "••••••",
                errorText          = state.passDiffError,
                isPassword         = true
            )

            FieldWithError(
                value       = state.name,
                onValue     = viewModel::changeName,
                label       = "Nombre",
                placeholder = "Nombre",
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
                onClick = { if(viewModel.validar()) viewModel.registrarEmpresa() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Registrarse")
            }
        }
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
                    IconButton (onClick = { visible = !visible }) {
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

