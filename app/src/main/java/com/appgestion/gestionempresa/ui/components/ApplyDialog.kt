package com.appgestion.gestionempresa.ui.components

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.appgestion.gestionempresa.data.model.Response
import com.appgestion.gestionempresa.navigation.AppScreen
import com.appgestion.gestionempresa.ui.perfiles.trabajador.cv.CurriculumViewModel

@Composable
fun ApplyDialog(
    workerId: String,
    ofertaId: String,
    onDismiss: () -> Unit,
    navController: NavController,
    viewModel: CurriculumViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val results by viewModel.applyResult.collectAsState(initial = null)

    LaunchedEffect(results) {
        results?.let {
            when (it) {
                is Response.Success ->
                    Toast.makeText(context, "CV enviado", Toast.LENGTH_SHORT).show()
                is Response.Failure ->
                    Toast.makeText(context, "Error: ${it.exception.message}", Toast.LENGTH_LONG).show()
                else -> {}
            }
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Selecciona tu CV") },
        text = {
            when (val resp = state.response) {
                is Response.Loading -> CircularProgressIndicator()
                is Response.Failure -> Text("Error: ${resp.exception.message}")
                is Response.Success -> Column {
                    resp.data.forEach { cv ->
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.applyToOffer(workerId, ofertaId, cv.id)
                                    Toast.makeText(context, "CV enviado", Toast.LENGTH_SHORT).show()
                                    onDismiss()
                                }
                                .padding(8.dp)
                        ) {
                            Text(cv.name, style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            Row() {
                TextButton(onClick = {
                    onDismiss()
                    navController.navigate(AppScreen.CVFormScreen.route)
                }) {
                    Text("Crear nuevo CV")
                }

                TextButton(
                    onClick = onDismiss
                ) {
                    Text("Cancelar")
                }
            }
        }
    )
}