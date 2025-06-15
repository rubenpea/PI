package com.appgestion.gestionempresa.ui.perfiles.trabajador.tareas

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.appgestion.gestionempresa.data.model.Response
import com.appgestion.gestionempresa.domain.model.TareaEntity
import com.appgestion.gestionempresa.ui.components.Dimens
import com.appgestion.gestionempresa.ui.perfiles.empresa.tareas.TareaViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun TareasTrabajadorScreen(
    viewModel: TareaViewModel = hiltViewModel()
) {
    val tareaState by viewModel.tareas.collectAsState()

    // Control para el diálogo de detalle
    var showDialog by remember { mutableStateOf(false) }
    var tareaSeleccionada by remember { mutableStateOf<TareaEntity?>(null) }

    LaunchedEffect(Unit) {
        FirebaseAuth.getInstance().currentUser?.uid?.let {
            viewModel.cargarTareasTrabajador(it)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = Dimens.padding)
    ) {
        Spacer(Modifier.height(Dimens.smallPadding))
        Text(
            text = "Mis tareas asignadas",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(Modifier.height(Dimens.padding))

        when (tareaState) {
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
                    text = "Error al cargar tareas",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error
                )
            }
            is Response.Success -> {
                val tareas = (tareaState as Response.Success<List<TareaEntity>>).data
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(Dimens.smallPadding),
                    contentPadding = PaddingValues(vertical = Dimens.smallPadding)
                ) {
                    items(tareas, key = { it.id }) { tarea ->
                        TareaCard(
                            tarea = tarea,
                            onClick = {
                                tareaSeleccionada = tarea
                                showDialog = true
                            }
                        )
                    }
                }
            }
        }
    }

    // Diálogo de detalle
    if (showDialog && tareaSeleccionada != null) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = {
                Text(text = tareaSeleccionada!!.titulo)
            },
            text = {
                Text(text = tareaSeleccionada!!.descripcion)
            },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cerrar")
                }
            }
        )
    }
}

@Composable
private fun TareaCard(
    tarea: TareaEntity,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.padding)
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(Dimens.padding)) {
            Text(
                text = tarea.titulo,
                style = MaterialTheme.typography.titleSmall
            )
            Spacer(Modifier.height(Dimens.smallPadding))
            StatusChip(status = tarea.estado.name)
        }
    }
}

@Composable
private fun StatusChip(status: String) {
    val color = when (status.uppercase()) {
        "PENDIENTE" -> MaterialTheme.colorScheme.secondary
        "COMPLETADA" -> MaterialTheme.colorScheme.primary
        "RECHAZADA" -> MaterialTheme.colorScheme.error
        else         -> MaterialTheme.colorScheme.onSurfaceVariant
    }
    Surface(
        color = color.copy(alpha = 0.1f),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.padding(top = Dimens.smallPadding)
    ) {
        Text(
            text      = status.lowercase().replaceFirstChar { it.uppercase() },
            style     = MaterialTheme.typography.labelMedium,
            color     = color,
            modifier  = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

