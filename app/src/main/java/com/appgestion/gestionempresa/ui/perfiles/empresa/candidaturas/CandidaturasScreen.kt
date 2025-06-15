package com.appgestion.gestionempresa.ui.perfiles.empresa.candidaturas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.appgestion.gestionempresa.data.model.Response
import com.appgestion.gestionempresa.domain.model.CandidaturaEntity
import com.appgestion.gestionempresa.navigation.AppScreen
import java.text.DateFormat
import java.util.Date
import androidx.compose.material3.*

@Composable
fun CandidaturasScreen(
    ofertaId: String,
    navController: NavController,
    viewModel: CandidaturasEmpresaViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(ofertaId) {
        viewModel.loadCandidaturas(ofertaId)
    }

    when (val resp = state.response) {
        is Response.Loading -> {
            Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        is Response.Failure -> {
            Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Error cargando candidatos", color = MaterialTheme.colorScheme.error)
            }
        }
        is Response.Success -> {
            val lista = resp.data
            if (lista.isEmpty()) {
                Box(
                    Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No hay candidaturas para esta oferta.")
                }
            } else {
                LazyColumn(
                    Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(lista) { cand ->
                        CandidaturaCard(
                            candidatura   = cand,
                            navController = navController,
                            onAccept      = { viewModel.updateStatus(cand.id, "ACEPTADA", ofertaId) },
                            onReject      = { viewModel.updateStatus(cand.id, "RECHAZADA", ofertaId) }
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun CandidaturaCard(
    candidatura: CandidaturaEntity,
    navController: NavController,
    onAccept: () -> Unit,
    onReject: () -> Unit,
    viewModel: CandidaturasEmpresaViewModel = hiltViewModel()
) {
    LaunchedEffect(candidatura.trabajadorId) {
        if (!viewModel.usuarios.containsKey(candidatura.trabajadorId)) {
            viewModel.fetchUsuario(candidatura.trabajadorId)
        }
    }

    val usuario = viewModel.usuarios[candidatura.trabajadorId]

    val sentDate = remember(candidatura.timestamp) {
        DateFormat.getDateTimeInstance().format(Date(candidatura.timestamp))
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (usuario != null) {
                Text(usuario.nombre,  style = MaterialTheme.typography.titleMedium)
                Text(usuario.email, style = MaterialTheme.typography.bodySmall)
                Text(usuario.telefono, style = MaterialTheme.typography.bodySmall)
            } else {
                Text(
                    "ID: ${candidatura.trabajadorId}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Enviado: $sentDate", style = MaterialTheme.typography.bodySmall)
                TextButton(onClick = {
                    navController.navigate(AppScreen.CvDetailScreen.createRoute(candidatura.cvId))
                }) {
                    Text("Ver CV")
                }
            }

            Divider()

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = onReject) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Rechazar",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
                IconButton(onClick = onAccept) {
                    Icon(
                        Icons.Default.Check,
                        contentDescription = "Aceptar",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}






