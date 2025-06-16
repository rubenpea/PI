package com.appgestion.gestionempresa.ui.perfiles.trabajador.fichaje

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.appgestion.gestionempresa.data.model.Response
import com.appgestion.gestionempresa.domain.model.FichajeEntity
import com.appgestion.gestionempresa.ui.components.Dimens
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt


@Composable
fun ResumenSemanalScreen(viewModel: FichajeViewModel = hiltViewModel()) {
    val uid = FirebaseAuth.getInstance().currentUser?.uid.orEmpty()
    val historial by viewModel.historial.collectAsState()

    LaunchedEffect(uid) {
        if (uid.isNotEmpty()) viewModel.cargarHistorial(uid)
    }

    val totalHorasSemana: Float = viewModel.calcularHorasSemanaActual()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = Dimens.padding)
    ) {
        Spacer(Modifier.height(Dimens.smallPadding))

        Text(
            text = "Resumen Semanal",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(Modifier.height(Dimens.padding))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = Dimens.smallPadding),
            shape = MaterialTheme.shapes.medium,
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Row(
                modifier = Modifier.padding(Dimens.padding),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Timer, contentDescription = null)
                Spacer(Modifier.width(Dimens.smallPadding))
                Column {
                    Text(
                        text = "Horas trabajadas",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "${totalHorasSemana.toInt()}h ${(totalHorasSemana % 1 * 60).roundToInt()}m",
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
            }
        }

        Spacer(Modifier.height(Dimens.padding))

        Text(
            text = "Detalle diario",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(Modifier.height(Dimens.smallPadding))

        when (historial) {
            is Response.Loading -> Box(
                Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            is Response.Success -> {
                val dias: List<Pair<String, List<FichajeEntity>>> =
                    (historial as Response.Success).data
                        .filter { it.duracion != null }
                        .groupBy { it.fecha }
                        .entries
                        .sortedByDescending { it.key }
                        .map { it.key to it.value }

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(Dimens.smallPadding),
                    contentPadding = PaddingValues(bottom = Dimens.padding)
                ) {
                    items(dias, key = { it.first }) { (fecha, registros) ->
                        DailySummaryCard(fecha = fecha, registros = registros)
                    }
                }
            }
            else -> Text(
                text = "No hay datos aún.",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(Modifier.height(Dimens.padding))
    }
}

@Composable
private fun DailySummaryCard(
    fecha: String,
    registros: List<FichajeEntity>
) {
    val sdfIn = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) }
    val sdfOut = remember { SimpleDateFormat("dd MMM yyyy", Locale.getDefault()) }
    val date = runCatching { sdfIn.parse(fecha) }.getOrNull() ?: Date()
    val fechaFormateada = sdfOut.format(date)

    val horasDia = registros
        .map { it.duracion!!.toFloat() / 1000f / 60f / 60f }
        .sum()
    val horasText = "%dh %02dm".format(horasDia.toInt(), ((horasDia % 1) * 60).roundToInt())

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.padding),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(Dimens.padding),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.CalendarToday, contentDescription = null)
            Spacer(Modifier.width(Dimens.smallPadding))
            Column {
                Text(
                    text = fechaFormateada,
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = horasText,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

