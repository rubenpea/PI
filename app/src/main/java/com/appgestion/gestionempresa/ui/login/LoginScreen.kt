package com.appgestion.gestionempresa.ui.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.appgestion.gestionempresa.navigation.AppScreen


@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel,
) {

    val state by viewModel.uiState.collectAsState()

    val error by viewModel.error.collectAsState()

    val user  by viewModel.user.collectAsState(initial = null)
    LaunchedEffect(user) {
        user?.let {
            val ruta = if (it.tipo == "empresa")
                AppScreen.HomeEmpresa.route
            else
                AppScreen.HomeTrabajador.route
            navController.navigate(ruta) {
                popUpTo(AppScreen.LoginScreen.route) { inclusive = true }
            }
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
                onValueChange = { viewModel.emailChange(it) },
                placeholder = { Text(text = "Login") },
                label = { Text(text = "Login") },
                modifier = Modifier
            )
            Spacer(
                modifier = Modifier
                    .padding(top = 8.dp)
            )

            OutlinedTextField(
                value = state.password,
                onValueChange = {viewModel.passChange(it)},
                placeholder = { Text(text = "Password") },
                label = { Text(text = "Password") },
                modifier = Modifier,
                visualTransformation = PasswordVisualTransformation()

            )
            Button(
                onClick = { viewModel.login() }
            ) {
                Text(text = "Login")
            }
            Row() {
                TextButton(
                    onClick = { navController.navigate(AppScreen.RegistroScreen.route) }
                ) { Text(text = "Regístrate") }
                TextButton(
                    onClick = { navController.navigate(AppScreen.RecupararScreen.route) }
                ) { Text(text = "¿Recuperar password?") }
            }
        }
    }

}




