package com.appgestion.gestionempresa.ui.perfiles.empresa

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.appgestion.gestionempresa.navigation.AppScreen
import com.appgestion.gestionempresa.ui.components.AppCard
import com.appgestion.gestionempresa.ui.components.FieldWithError
import com.google.firebase.auth.FirebaseAuth

@Composable
fun EmpresaTrabajadoresScreen(
    navController: NavController,
    viewModel: EmpresaViewModel = hiltViewModel()
) {
    val trabajadores by viewModel.trabajadoresInfo.collectAsState()
    val empresaId = FirebaseAuth.getInstance().currentUser!!.uid

    LaunchedEffect(empresaId) {
        viewModel.cargarTrabajadoresVinculados(empresaId)
    }

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text("Gestionar Trabajadores", style = MaterialTheme.typography.headlineSmall)

        AppCard(modifier = Modifier.fillMaxWidth()) {
            var correo by rememberSaveable { mutableStateOf("") }
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Añadir Trabajador", style = MaterialTheme.typography.titleMedium)
                FieldWithError(
                    value         = correo,
                    onValueChange = { correo = it },
                    label         = "Correo trabajador",
                    placeholder   = "usuario@correo.com",
                    errorText     = null,
                    modifier      = Modifier.fillMaxWidth()
                )
                Button(
                    onClick = {
                        viewModel.agregarTrabajador(correo.trim(), empresaId)
                        correo = ""
                    },
                    Modifier.fillMaxWidth()
                ) { Text("Agregar") }
            }
        }

        AppCard(modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Trabajadores vinculados", style = MaterialTheme.typography.titleMedium)
                if (trabajadores.isEmpty()) {
                    Text("No hay trabajadores asociados.", style = MaterialTheme.typography.bodySmall)
                } else {
                    LazyColumn(
                        modifier = Modifier.heightIn(max = 200.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(trabajadores) { t ->
                            Row(
                                Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    "${t.nombre} (${t.email})",
                                    Modifier.weight(1f),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                IconButton (onClick = {
                                    viewModel.eliminarTrabajador(empresaId, t.id)
                                } )icon@{
                                    Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                                }
                            }
                            Divider()
                        }
                    }
                }
            }
        }

        AppCard(modifier = Modifier.fillMaxWidth()) {
            Column(
                Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Tareas", style = MaterialTheme.typography.titleMedium)
                Text(
                    "Aquí podrás asignar tareas a tus trabajadores.",
                    style = MaterialTheme.typography.bodySmall
                )
                Button(
                    onClick = {
                        navController.navigate(AppScreen.TareasEmpresaScreen.createRoute(empresaId))
                    },
                    Modifier.fillMaxWidth()
                ) { Text("Asignar Tarea") }
            }
        }
    }
}
