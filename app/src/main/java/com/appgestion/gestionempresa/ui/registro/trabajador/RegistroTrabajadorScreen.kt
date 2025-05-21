package com.appgestion.gestionempresa.ui.registro.trabajador

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.appgestion.gestionempresa.navigation.AppScreen
import dagger.hilt.android.lifecycle.HiltViewModel

@Composable
fun RegistroTrabajadorScreen(
    navController: NavController,
    viewModel: RegistroTrabajadorViewModel = hiltViewModel()
) {

    val state by viewModel.uiState.collectAsState()
    val success by viewModel.registroSuccess.collectAsState()

    LaunchedEffect(success) {
        if (success) {
            Log.d("RegistroUI", "Entró al LaunchedEffect con success=$success")
            navController.navigate(AppScreen.HomeTrabajador.route) {
                popUpTo(AppScreen.LoginScreen.route) { inclusive = true }
            }
            viewModel.resetSuccess()
        }
    }

    Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier,
            verticalArrangement = Arrangement.Center

        ) {
            OutlinedTextField(
                value = state.email,
                onValueChange = { viewModel.changeEmail(it) },
                placeholder = { Text(text = "Email") },
                label = { Text(text = "Email") },
                isError = state.emailError != null,
                modifier = Modifier
            )
            val emailHintText = state.emailError ?: "Campo obligatorio"
            val emailHintColor = if (state.emailError != null) {
                MaterialTheme.colorScheme.error
            } else {
                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            }

            Text(
                text = emailHintText,
                color = emailHintColor,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .padding(top = 8.dp, start = 8.dp)
            )

            Spacer(
                modifier = Modifier
                    .padding(top = 8.dp)
            )

            OutlinedTextField(
                value = state.password,
                onValueChange = { viewModel.changePass(it) },
                placeholder = { Text(text = "Password") },
                label = { Text(text = "Password") },
                isError = state.passwordError != null || state.passDiffError != null,
                modifier = Modifier
            )
            val passwordHintText = state.passwordError ?: "*Campo obligatorio"
            val passwordHintColor = if (state.passwordError != null) {
                MaterialTheme.colorScheme.error
            } else {
                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            }

            Text(
                text = passwordHintText,
                color = passwordHintColor,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 8.dp, start = 8.dp)
            )

            Spacer(
                modifier = Modifier
                    .padding(top = 8.dp)
            )

            OutlinedTextField(
                value = state.passwordConfirm,
                onValueChange = { viewModel.changePassConfirm(it) },
                placeholder = { Text(text = "Password Repeat") },
                label = { Text(text = "Password Repeat") },
                isError = state.passDiffError != null,
                modifier = Modifier
            )

            val passwordConfirmHintText = state.passDiffError ?: "*Repite la contraseña"
            val passwordConfirmHintColor = if (state.passDiffError != null) {
                MaterialTheme.colorScheme.error
            } else {
                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            }

            Text(
                text = passwordConfirmHintText,
                color = passwordConfirmHintColor,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 8.dp, start = 8.dp)
            )

            Spacer(
                modifier = Modifier
                    .padding(top = 8.dp)
            )

            OutlinedTextField(
                value = state.name,
                onValueChange = { viewModel.changeName(it) },
                placeholder = { Text(text = "Nombre") },
                label = { Text(text = "Nombre") },
                isError = state.nameError != null,
                modifier = Modifier
            )
            state.nameError?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .padding(top = 8.dp, start = 8.dp)
                )
            }
            Spacer(
                modifier = Modifier
                    .padding(top = 8.dp)
            )

            OutlinedTextField(
                value = state.phone,
                onValueChange = { viewModel.changePhone(it) },
                placeholder = { Text(text = "Teléfono") },
                label = { Text(text = "Teléfono") },
                isError = state.phoneError != null,
                modifier = Modifier
            )
            state.phoneError?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .padding(top = 8.dp, start = 8.dp)
                )
            }
            Row() {
                Button(
                    onClick = {
                        if (viewModel.validar()) {
                            viewModel.registrarTrabajador()
                        }
                    }
                ) {
                    Text(text = "Registrarse")
                }
            }

        }
    }

}

@Preview
@Composable
fun preview() {
}