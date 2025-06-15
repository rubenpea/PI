package com.appgestion.gestionempresa.ui.perfiles.trabajador.ofertas

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.appgestion.gestionempresa.data.model.Response
import com.appgestion.gestionempresa.ui.components.ApplyDialog
import com.appgestion.gestionempresa.ui.perfiles.trabajador.cv.CurriculumViewModel
import com.google.firebase.auth.FirebaseAuth
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OfferDetailScreen(
    ofertaId: String,
    navController: NavController,
    viewModel: OfferDetailViewModel = hiltViewModel(),
    cvVM: CurriculumViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val workerId = FirebaseAuth.getInstance().currentUser?.uid.orEmpty()
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(ofertaId) {
        viewModel.loadOferta(ofertaId)
    }

    Box(Modifier.fillMaxSize().padding(16.dp)) {
        when (val resp = state.response) {
            is Response.Loading ->
                CircularProgressIndicator(Modifier.align(Alignment.Center))

            is Response.Failure ->
                Text(
                    "Error: ${resp.exception.localizedMessage}",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center)
                )

            is Response.Success -> {
                val o = resp.data
                Column(
                    Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Título
                    Text(o.titulo, style = MaterialTheme.typography.headlineSmall)

                    // Descripción
                    Text(o.descripcion, style = MaterialTheme.typography.bodyLarge)

                    // Requisitos
                    Text("Requisitos", style = MaterialTheme.typography.titleMedium)
                    Text(o.requisitos, style = MaterialTheme.typography.bodyMedium)

                    // Salario
                    Text("Salario", style = MaterialTheme.typography.titleMedium)
                    Text("${o.salario} €", style = MaterialTheme.typography.bodyMedium)

                    // Publicación
                    val fechaPub = Instant.ofEpochMilli(o.fechaPublicacion)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                        .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                    Text("Publicado el $fechaPub", style = MaterialTheme.typography.bodySmall)

                    Spacer(Modifier.weight(1f))

                    // Botón Inscribirse
                    Button(
                        onClick = {
                            cvVM.loadMyCVs(workerId)
                            showDialog = true
                        },
                        Modifier.fillMaxWidth()
                    ) {
                        Text("Inscribirme")
                    }
                }
            }
        }

        // Dialog de selección de CV / confirmación
        if (showDialog) {
            ApplyDialog(
                workerId    = workerId,
                ofertaId    = ofertaId,
                onDismiss   = { showDialog = false },
                viewModel   = cvVM,
                navController = navController
            )
        }
    }
}
