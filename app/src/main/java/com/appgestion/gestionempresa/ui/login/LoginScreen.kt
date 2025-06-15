package com.appgestion.gestionempresa.ui.login

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
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.appgestion.gestionempresa.R
import com.appgestion.gestionempresa.domain.model.Role
import com.appgestion.gestionempresa.navigation.AppScreen
import com.appgestion.gestionempresa.ui.components.PasswordField


@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val state       by viewModel.uiState.collectAsState()
    val usuario     by viewModel.user.collectAsState()
    val errorMessage by viewModel.error.collectAsState()

<<<<<<< HEAD
    val state by viewModel.uiState.collectAsState()

    val error by viewModel.error.collectAsState()

    val user  by viewModel.user.collectAsState(initial = null)
    LaunchedEffect(user) {
        user?.let {
            val ruta = if (it.tipo == "empresa")
=======
    // Navegación tras login exitoso
    LaunchedEffect(usuario) {
        usuario?.let { user ->
            val ruta = if (user.rol == Role.EMPRESA)
>>>>>>> c40b17f (Proyecto Entrega)
                AppScreen.HomeEmpresa.route
            else
                AppScreen.HomeTrabajador.route
            navController.navigate(ruta) {
                popUpTo(AppScreen.LoginScreen.route) { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier            = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter            = painterResource(R.drawable.logo),
                contentDescription = "Logo App",
                modifier           = Modifier
                    .size(100.dp)
                    .padding(bottom = 32.dp)
            )

            Text(
                text           = "Iniciar sesión",
                style          = MaterialTheme.typography.headlineSmall,
                color          = MaterialTheme.colorScheme.primary,
                modifier       = Modifier.padding(bottom = 24.dp)
            )

            OutlinedTextField(
                value           = state.email,
                onValueChange   = { viewModel.emailChange(it) },
                label           = { Text("Email") },
                placeholder     = { Text("usuario@correo.com") },
                isError         = errorMessage != null && state.email.isBlank(),
                singleLine      = true,
                modifier        = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))
            PasswordField(
                password         = state.password,
                onPasswordChange = { viewModel.passChange(it) },
                modifier         = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(24.dp))

            Button(
                onClick  = { viewModel.login() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Iniciar sesión")
            }

            Spacer(Modifier.height(16.dp))
            Row(
                modifier            = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(onClick =  { navController.navigate(AppScreen.RegistroEmpresaScreen.route) } ){
                    Text("Empresa")
                }
                TextButton(onClick =  { navController.navigate(AppScreen.RegistroTrabajadorScreen.route) } ){
                    Text("Trabajador")
                }
            }
            Spacer(Modifier.height(8.dp))
            TextButton (onClick = { navController.navigate(AppScreen.RecupararScreen.route) }){
                Text("¿Olvidaste la contraseña?")
            }

            errorMessage?.let { msg ->
                Spacer(Modifier.height(16.dp))
                Text(
                    text  = msg,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}





