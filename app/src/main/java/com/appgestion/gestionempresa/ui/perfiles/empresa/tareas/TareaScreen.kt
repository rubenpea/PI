package com.appgestion.gestionempresa.ui.perfiles.empresa.tareas

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.appgestion.gestionempresa.data.model.Response
import com.appgestion.gestionempresa.domain.model.TareaEntity
import com.appgestion.gestionempresa.domain.model.UsuarioEntity
import com.appgestion.gestionempresa.ui.perfiles.empresa.EmpresaViewModel
import androidx.compose.material3.*

@Composable
fun AsignarTareaScreen(
    empresaId: String,
    navController: NavController,
    empresaViewModel: EmpresaViewModel = hiltViewModel(),
    tareaViewModel: TareaViewModel = hiltViewModel()
) {
    val trabajadores    by empresaViewModel.trabajadoresInfo.collectAsState()
    val tareasState     by tareaViewModel.tareas.collectAsState()
    var searchQuery     by rememberSaveable { mutableStateOf("") }
    var showCreateDialog by rememberSaveable { mutableStateOf(false) }
    var nuevoTitulo     by rememberSaveable { mutableStateOf("") }
    var nuevaDesc       by rememberSaveable { mutableStateOf("") }
    var seleccionado    by rememberSaveable { mutableStateOf<UsuarioEntity?>(null) }

    var expandedTareaId by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(empresaId) {
        empresaViewModel.cargarTrabajadoresVinculados(empresaId)
        tareaViewModel.cargarTareasEmpresa(empresaId)
    }

    Box(Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxSize()) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholder = { Text("Buscar…") },
                singleLine = true,
                trailingIcon = {
                    if (searchQuery.isNotBlank()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Close, contentDescription = "Limpiar")
                        }
                    }
                }
            )

            when (tareasState) {
                is Response.Loading -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is Response.Failure -> {
                    Text(
                        "Error cargando tareas",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                is Response.Success -> {
                    val todas = (tareasState as Response.Success<List<TareaEntity>>).data
                    val filtradas = todas.filter { tarea ->
                        val nombreTrab = trabajadores
                            .firstOrNull { it.id == tarea.trabajadorId }
                            ?.nombre.orEmpty()
                        tarea.titulo.contains(searchQuery, true)
                                || nombreTrab.contains(searchQuery, true)
                    }

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item {
                            Text(
                                "Tareas asignadas",
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    color = MaterialTheme.colorScheme.primary
                                )
                            )
                        }

                        if (filtradas.isEmpty()) {
                            item {
                                Text(
                                    "No hay tareas.",
                                    modifier = Modifier.padding(16.dp),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        } else {
                            items(filtradas) { tarea ->
                                val nombreTrab = trabajadores
                                    .firstOrNull { it.id == tarea.trabajadorId }
                                    ?.nombre
                                    ?: tarea.trabajadorId.take(6) + "…"

                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            expandedTareaId =
                                                if (expandedTareaId == tarea.id) null else tarea.id
                                        },
                                    elevation = CardDefaults.cardElevation(2.dp)
                                ) {
                                    Column(
                                        Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Column(modifier = Modifier.weight(1f)) {
                                                Text(
                                                    tarea.titulo,
                                                    style = MaterialTheme.typography.titleMedium.copy(
                                                        color = MaterialTheme.colorScheme.primary
                                                    )
                                                )
                                                Text(
                                                    "Para: $nombreTrab",
                                                    style = MaterialTheme.typography.bodySmall
                                                )
                                                Text(
                                                    "Estado: ${tarea.estado.name.lowercase().replaceFirstChar { it.uppercase() }}",
                                                    style = MaterialTheme.typography.bodySmall.copy(
                                                        color = MaterialTheme.colorScheme.secondary
                                                    )
                                                )

                                            }
                                            Icon(
                                                imageVector = if (expandedTareaId == tarea.id)
                                                    Icons.Default.ExpandLess
                                                else
                                                    Icons.Default.ExpandMore,
                                                contentDescription = if (expandedTareaId == tarea.id)
                                                    "Ocultar" else "Expandir"
                                            )
                                        }
                                        if (expandedTareaId == tarea.id) {
                                            Divider()
                                            Text(
                                                tarea.descripcion,
                                                style = MaterialTheme.typography.bodyMedium
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = {
                nuevoTitulo = ""
                nuevaDesc = ""
                seleccionado = null
                showCreateDialog = true
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Crear Tarea")
        }

        if (showCreateDialog) {
            Dialog(onDismissRequest = { showCreateDialog = false }) {
                Surface(shape = RoundedCornerShape(8.dp)) {
                    Column(
                        Modifier
                            .padding(16.dp)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text("Nueva tarea", style = MaterialTheme.typography.titleMedium)
                        OutlinedTextField(
                            value = nuevoTitulo,
                            onValueChange = { nuevoTitulo = it },
                            label = { Text("Título") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = nuevaDesc,
                            onValueChange = { nuevaDesc = it },
                            label = { Text("Descripción") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Text("Asignar a:", style = MaterialTheme.typography.bodyMedium)
                        trabajadores.forEach { t ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { seleccionado = t }
                                    .padding(vertical = 4.dp)
                            ) {
                                RadioButton(
                                    selected = seleccionado?.id == t.id,
                                    onClick = { seleccionado = t }
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(t.nombre)
                            }
                        }
                        Spacer(Modifier.height(8.dp))
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                            TextButton(onClick = { showCreateDialog = false }) {
                                Text("Cancelar")
                            }
                            Spacer(Modifier.width(8.dp))
                            Button(
                                onClick = {
                                    seleccionado?.let {
                                        tareaViewModel.crearTarea(
                                            titulo = nuevoTitulo,
                                            descripcion = nuevaDesc,
                                            trabajadorId = it.id,
                                            empresaId = empresaId
                                        )
                                        tareaViewModel.cargarTareasEmpresa(empresaId)
                                        showCreateDialog = false
                                    }
                                },
                                enabled = nuevoTitulo.isNotBlank() && seleccionado != null
                            ) {
                                Text("Crear")
                            }
                        }
                    }
                }
            }
        }
    }
}



