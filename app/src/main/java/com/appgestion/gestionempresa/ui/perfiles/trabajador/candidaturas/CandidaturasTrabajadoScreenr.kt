package com.appgestion.gestionempresa.ui.perfiles.trabajador.candidaturas

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.appgestion.gestionempresa.data.model.Response
import com.appgestion.gestionempresa.domain.model.CandidaturaEntity
import com.appgestion.gestionempresa.navigation.AppScreen
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun CandidaturasTrabajadorScreen(
    viewModel: CandidaturasTrabajadorViewModel = hiltViewModel(),
    navController: NavController
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        val workerId = FirebaseAuth.getInstance().currentUser!!.uid
        viewModel.loadCandidaturas(workerId)
    }

        when (val resp = state.response) {
            is Response.Loading -> {
                Box(
                    Modifier
                        .fillMaxSize()
                        .padding(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is Response.Failure -> {
                Box(
                    Modifier
                        .fillMaxSize()
                        .padding(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Error: ${resp.exception.localizedMessage}")
                }
            }
            is Response.Success -> {
                LazyColumn(
                    Modifier
                        .fillMaxSize()
                        .padding(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(resp.data, key = { it.id }) { cand ->
                        CandidaturaRow(
                            candidatura      = cand,
                            onClickViewOffer = {
                                navController.navigate(
                                    AppScreen.OfertaDetailScreen.createRoute(cand.ofertaId)
                                )
                            }
                        )
                    }
                }
            }
        }
    }


@Composable
fun CandidaturaRow(
    candidatura: CandidaturaEntity,
    onClickViewOffer: () -> Unit
) {
    val sdf = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }

    Card(
        onClick = onClickViewOffer,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(
                text = "Oferta: ${candidatura.ofertaId}",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Enviado: ${sdf.format(Date(candidatura.timestamp))}",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(Modifier.height(8.dp))
            // Chip de estado
            val color = when (candidatura.status.uppercase()) {
                "ACEPTADA"  -> MaterialTheme.colorScheme.primary
                "RECHAZADA" -> MaterialTheme.colorScheme.error
                else        -> MaterialTheme.colorScheme.secondary
            }
            Surface(
                color = color.copy(alpha = 0.1f),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.padding(top = 4.dp)
            ) {
                Text(
                    text = candidatura.status.lowercase()
                        .replaceFirstChar { it.uppercase() },
                    style = MaterialTheme.typography.labelMedium,
                    color = color,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}
