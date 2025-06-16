package com.appgestion.gestionempresa.ui.perfiles.empresa.candidaturas

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.appgestion.gestionempresa.data.model.Response
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.getValue


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CvDetailScreen(
    cvId: String,
    navController: NavController,
    viewModel: CvDetailViewModel = hiltViewModel()
) {
    // Carga inicial
    LaunchedEffect(cvId) {
        viewModel.loadCv(cvId)
    }

    val uiState by viewModel.uiState.collectAsState()

    Box(Modifier.fillMaxSize()) {
        when (val resp = uiState) {
            is Response.Loading -> {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            }
            is Response.Failure -> {
                Text(
                    "Error: ${resp.exception.localizedMessage}",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            is Response.Success -> {
                val cv = resp.data
                Column(
                    Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(cv.name, style = MaterialTheme.typography.headlineSmall)
                    Divider()

                    Text("Email: ${cv.email}",    style = MaterialTheme.typography.bodyMedium)
                    Text("Teléfono: ${cv.phone}", style = MaterialTheme.typography.bodyMedium)

                    Divider()

                    Text("Resumen", style = MaterialTheme.typography.titleMedium)
                    Text(cv.summary, style = MaterialTheme.typography.bodySmall)

                    Divider()

                    Text("Habilidades", style = MaterialTheme.typography.titleMedium)
                    cv.skills.forEach { skill ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("•", modifier = Modifier.padding(end = 8.dp))
                            Text(skill, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
    }
}






