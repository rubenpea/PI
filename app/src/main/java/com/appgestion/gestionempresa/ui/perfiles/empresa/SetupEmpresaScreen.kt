package com.appgestion.gestionempresa.ui.perfiles.empresa

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.appgestion.gestionempresa.R
import com.appgestion.gestionempresa.domain.model.EmpresaEntity
import com.appgestion.gestionempresa.navigation.AppScreen
import com.appgestion.gestionempresa.ui.components.FieldWithError
import com.google.firebase.auth.FirebaseAuth

@Composable
fun SetupEmpresaScreen(
    navController: NavController,
    viewModel: EmpresaViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val currentUid = FirebaseAuth.getInstance().currentUser?.uid

    var nombre by remember { mutableStateOf(state.empresa?.nombre.orEmpty()) }
    var descripcion by remember { mutableStateOf(state.empresa?.descripcion.orEmpty()) }

    LaunchedEffect(state.successMessage) {
        if (state.successMessage != null) {
            navController.navigate(AppScreen.HomeEmpresa.route) {
                popUpTo(AppScreen.RegistroEmpresaScreen.route) { inclusive = true }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(
            painter = painterResource(R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier.size(72.dp)
        )
        Text(
            text = "Configura tu empresa",
            style = MaterialTheme.typography.headlineSmall
        )

        FieldWithError(
            value        = nombre,
            onValueChange= { nombre = it },
            label        = "Nombre de la empresa",
            errorText    = if (nombre.isBlank()) "Este campo es obligatorio" else null
        )
        FieldWithError(
            value        = descripcion,
            onValueChange= { descripcion = it },
            label        = "Descripción",
            placeholder  = "Breve descripción",
            errorText    = if (descripcion.isBlank()) "Este campo es obligatorio" else null,
            singleLine   = false,
            height       = 100.dp
        )

        Spacer(Modifier.weight(1f))

        Button(
            onClick = {
                currentUid?.let { uid ->
                    viewModel.saveEmpresa(
                        EmpresaEntity(
                            id                = uid,
                            nombre            = nombre.trim(),
                            descripcion       = descripcion.trim(),
                            fechaCreacion     = state.empresa?.fechaCreacion ?: System.currentTimeMillis(),
                            listaTrabajadores = state.empresa?.listaTrabajadores ?: emptyList()
                        )
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text("Guardar Perfil")
        }

        if (state.loading) {
            Spacer(Modifier.height(16.dp))
            CircularProgressIndicator()
        }
        state.error?.let { err ->
            Spacer(Modifier.height(8.dp))
            Text(
                text  = err,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
