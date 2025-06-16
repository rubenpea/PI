package com.appgestion.gestionempresa.ui.perfiles.trabajador.ofertas

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.appgestion.gestionempresa.data.model.Response
import com.appgestion.gestionempresa.navigation.AppScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OffersScreen(
    navController: NavController,
    viewModel: OffersViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val empresaNames by remember { derivedStateOf { viewModel.empresaNames } }
    var query by remember { mutableStateOf("") }

    Column(Modifier.fillMaxSize()) {
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            label = { Text("Buscar categoría o título") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            singleLine = true,
            trailingIcon = {
                if (query.isNotBlank()) {
                    IconButton(onClick = { query = "" }) {
                        Icon(Icons.Default.Close, contentDescription = "Borrar búsqueda")
                    }
                }
            },
            keyboardOptions  = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions  = KeyboardActions(onSearch = {
                viewModel.filter(query.trim())
            })
        )

        Box(Modifier.fillMaxSize()) {
            when (val resp = uiState.response) {
                is Response.Loading -> {
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                }
                is Response.Failure -> {
                    Text(
                        "Error: ${resp.exception.localizedMessage}",
                        color    = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is Response.Success -> {
                    val ofertas = if (query.isBlank()) {
                        resp.data
                    } else {
                        resp.data.filter {
                            it.titulo.contains(query, ignoreCase = true) ||
                                    it.requisitos.contains(query, ignoreCase = true)
                        }
                    }

                    LazyColumn(
                        contentPadding     = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(ofertas) { oferta ->
                            // Recupera el nombre de la empresa o un placeholder
                            val nombreEmpresa = empresaNames[oferta.idEmpresa].orEmpty().ifBlank { "Empresa" }

                            OfertaCard(
                                oferta         = oferta,
                                empresaNombre  = nombreEmpresa,
                                onClick        = {
                                    navController.navigate(
                                        AppScreen.OfertaDetailScreen.createRoute(oferta.id)
                                    )
                                }
                            )
                        }
                        if (ofertas.isEmpty()) {
                            item {
                                Text(
                                    "No se han encontrado ofertas.",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(24.dp),
                                    textAlign = TextAlign.Center,
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



