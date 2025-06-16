package com.appgestion.gestionempresa.ui.perfiles.empresa.vacaciones

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.appgestion.gestionempresa.data.model.Response
import com.appgestion.gestionempresa.ui.perfiles.empresa.EmpresaViewModel
import java.text.SimpleDateFormat
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun VacacionesEmpresaScreen(
    empresaId: String,
    navController: NavController,
    viewModel: VacacionesEmpresaViewModel = hiltViewModel(),
    empresaViewModel: EmpresaViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val trabajadores by empresaViewModel.trabajadoresInfo.collectAsState()

    LaunchedEffect(empresaId) {
        empresaViewModel.cargarTrabajadoresVinculados(empresaId)
        viewModel.loadRequests(empresaId)
    }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "Vacaciones Pedidas",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        when (val resp = state.response) {
            is Response.Loading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is Response.Failure -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        "Error: ${resp.exception.localizedMessage}",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
            is Response.Success -> {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(resp.data) { req ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Row(
                                Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    val nombreTrabajador = trabajadores
                                        .firstOrNull { it.id == req.workerId }
                                        ?.nombre
                                        ?.replaceFirstChar { it.uppercase() }
                                        ?: req.workerId

                                    Text(
                                        nombreTrabajador,
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Text(
                                        "Desde ${SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(req.startDate))}",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                    Text(
                                        "Hasta ${SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(req.endDate))}",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                    Text(
                                        "Días: ${req.days}",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                    Text(
                                        req.status.replaceFirstChar { it.uppercase() },
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            color = when (req.status) {
                                                "PENDIENTE" -> MaterialTheme.colorScheme.primary
                                                "ACEPTADA"  -> MaterialTheme.colorScheme.secondary
                                                "RECHAZADA" -> MaterialTheme.colorScheme.error
                                                else        -> MaterialTheme.colorScheme.onSurface
                                            }
                                        )
                                    )
                                }

                                Column(
                                    horizontalAlignment = Alignment.End,
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    IconButton(onClick = { viewModel.updateRequestStatus(req.id, "ACEPTADA", empresaId) }) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = "Aceptar",
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                    IconButton(onClick = { viewModel.updateRequestStatus(req.id, "RECHAZADA", empresaId) }) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "Rechazar",
                                            tint = MaterialTheme.colorScheme.error
                                        )
                                    }
                                }
                            }
                        }
                    }
                    if (resp.data.isEmpty()) {
                        item {
                            Text(
                                "No hay peticiones de vacaciones.",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
