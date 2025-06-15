package com.appgestion.gestionempresa.ui.perfiles.trabajador.vacaciones

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.appgestion.gestionempresa.data.model.Response
import com.appgestion.gestionempresa.domain.model.VacacionesEntity
import com.appgestion.gestionempresa.ui.components.Dimens
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun VacacionesSolicitadasScreen(
    viewModel: VacacionesSolicitadasViewModel = hiltViewModel(),
    navController: NavController
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        FirebaseAuth.getInstance().currentUser?.uid
            ?.let { viewModel.loadRequests(it) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = Dimens.padding)
    ) {
        Spacer(Modifier.height(Dimens.smallPadding))


        Spacer(Modifier.height(Dimens.padding))

        when (val resp = state.response) {
            is Response.Loading -> Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }

            is Response.Failure -> Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Error: ${resp.exception.localizedMessage}",
                    color = MaterialTheme.colorScheme.error
                )
            }

            is Response.Success -> {
                val requests = resp.data
                if (requests.isEmpty()) {
                    Box(
                        Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No hay solicitudes aún.")
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(Dimens.smallPadding),
                        contentPadding = PaddingValues(vertical = Dimens.smallPadding)
                    ) {
                        items(requests, key = { it.id }) { req ->
                            VacacionRequestCard(req)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun VacacionRequestCard(req: VacacionesEntity) {
    // Formateadores de fecha
    val inFmt  = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) }
    val outFmt = remember { SimpleDateFormat("dd MMM yyyy", Locale.getDefault()) }

    // Parseo seguro
    val startDate = runCatching { Date(inFmt.parse(req.startDate.toString())!!.time) }
        .getOrNull() ?: Date()
    val endDate   = runCatching { Date(inFmt.parse(req.endDate.toString())!!.time) }
        .getOrNull() ?: startDate

    // Texto de rango
    val rangeText = "${outFmt.format(startDate)} → ${outFmt.format(endDate)}"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.padding),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(Dimens.padding)) {
            // Rango de fechas
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.DateRange, contentDescription = null)
                Spacer(Modifier.width(Dimens.smallPadding))
                Text(
                    text = rangeText,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Spacer(Modifier.height(Dimens.smallPadding))

            // Días y estado
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.CalendarToday, contentDescription = null)
                Spacer(Modifier.width(Dimens.smallPadding))
                Text(
                    text = "Días: ${req.days}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(Modifier.weight(1f))
                StatusChip(status = req.status)
            }
        }
    }
}

@Composable
private fun StatusChip(status: String) {
    val color = when (status.uppercase()) {
        "PENDIENTE" -> MaterialTheme.colorScheme.secondary
        "ACEPTADA"  -> MaterialTheme.colorScheme.primary
        "RECHAZADA" -> MaterialTheme.colorScheme.error
        else        -> MaterialTheme.colorScheme.onSurfaceVariant
    }
    Surface(
        color = color.copy(alpha = 0.1f),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = status.lowercase().replaceFirstChar { it.uppercase() },
            style = MaterialTheme.typography.labelMedium,
            color = color,
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

