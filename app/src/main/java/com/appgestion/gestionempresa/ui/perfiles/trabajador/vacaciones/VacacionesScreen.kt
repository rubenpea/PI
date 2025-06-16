package com.appgestion.gestionempresa.ui.perfiles.trabajador.vacaciones

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.appgestion.gestionempresa.data.model.Response
import com.appgestion.gestionempresa.domain.model.VacacionesEntity
import com.appgestion.gestionempresa.navigation.AppScreen
import com.google.firebase.auth.FirebaseAuth
import java.util.Date
import androidx.compose.material3.DatePickerDialog
import com.appgestion.gestionempresa.ui.components.Dimens
import java.text.SimpleDateFormat
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun VacacionesScreen(
    navController: NavController,
    vacacionesViewModel: VacacionesViewModel = hiltViewModel(),
    vacacionesSolicitadasViewModel: VacacionesSolicitadasViewModel = hiltViewModel()
) {
    val formState  by vacacionesViewModel.state.collectAsState()
    val histoState by vacacionesSolicitadasViewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        FirebaseAuth.getInstance().currentUser?.uid
            ?.let { vacacionesSolicitadasViewModel.loadRequests(it) }
    }

    if (histoState.response is Response.Loading) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val pendientes = (histoState.response as? Response.Success<List<VacacionesEntity>>)
        ?.data
        ?.any { it.status == "PENDIENTE" } == true

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = Dimens.padding)
    ) {
        Spacer(Modifier.height(Dimens.smallPadding))

        if (pendientes) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(Modifier.padding(Dimens.padding)) {
                    Text(
                        text = "Tienes una solicitud de vacaciones pendiente. Cuando sea procesada podrás enviar una nueva.",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(Modifier.height(Dimens.padding))
                    Button(
                        onClick = { navController.navigate(AppScreen.VacacionesSolicitadasScreen.route) },
                        modifier = Modifier.fillMaxWidth().height(48.dp)
                    ) {
                        Text("Ver historial de vacaciones")
                    }
                }
            }

        } else {
            var openDialog by remember { mutableStateOf(false) }
            val dateState = rememberDateRangePickerState()

            Button(
                onClick = { openDialog = true },
                modifier = Modifier.fillMaxWidth().height(48.dp)
            ) {
                Text("Selecciona rango de vacaciones")
            }

            if (openDialog) {
                DatePickerDialog(
                    onDismissRequest = { openDialog = false },
                    confirmButton = {
                        TextButton(onClick = {
                            dateState.selectedStartDateMillis?.let { start ->
                                dateState.selectedEndDateMillis?.let { end ->
                                    vacacionesViewModel.onDatesChanged(start, end)
                                }
                            }
                            openDialog = false
                        }) { Text("OK") }
                    },
                    dismissButton = {
                        TextButton(onClick = { openDialog = false }) { Text("Cancelar") }
                    }
                ) {
                    DateRangePicker(state = dateState)
                }
            }

            formState.startDate?.let { startMillis ->
                val sdf = remember { SimpleDateFormat("dd MMM yyyy", Locale.getDefault()) }
                val startDate = Date(startMillis)
                val endDate   = Date(formState.endDate ?: startMillis)

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = Dimens.smallPadding),
                    shape = MaterialTheme.shapes.medium,
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(Modifier.padding(Dimens.padding)) {
                        Text("Desde", style = MaterialTheme.typography.titleMedium)
                        Text(sdf.format(startDate), style = MaterialTheme.typography.bodyMedium)
                        Spacer(Modifier.height(Dimens.smallPadding))
                        Text("Hasta", style = MaterialTheme.typography.titleMedium)
                        Text(sdf.format(endDate), style = MaterialTheme.typography.bodyMedium)
                        Spacer(Modifier.height(Dimens.smallPadding))
                        Text(
                            text = "Días: ${formState.days}",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }

                Spacer(Modifier.height(Dimens.padding))

                Button(
                    onClick = {
                        FirebaseAuth.getInstance().currentUser?.uid?.let {
                            vacacionesViewModel.submit(it)
                            navController.navigate(AppScreen.VacacionesSolicitadasScreen.route)
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(48.dp)
                ) {
                    Text("Enviar petición")
                }
            }
        }
    }
}

