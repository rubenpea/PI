package com.appgestion.gestionempresa.ui.perfiles.empresa.ofertas

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.appgestion.gestionempresa.data.model.Response
import com.appgestion.gestionempresa.navigation.AppScreen

@Composable
fun MisOfertasScreen(
    empresaId: String,
    viewModel: OfertaViewModel = hiltViewModel(),
    onVerDetalle: (String) -> Unit,
    navController: NavController
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(empresaId) {
        viewModel.loadOfertas(empresaId)
    }

    Box(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        when (val resp = state.ofertasResponse) {
            is Response.Loading ->
                CircularProgressIndicator(Modifier.align(Alignment.Center))

            is Response.Failure ->
                Text(
                    text = "Error: ${resp.exception.localizedMessage}",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center)
                )

            is Response.Success -> {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(resp.data) { oferta ->
                        Card(
                            Modifier
                                .fillMaxWidth()
                                .clickable {   navController.navigate(AppScreen.CandidaturasOfertaScreen.createRoute(oferta.id))
                                }
                        ) {
                            Column(
                                Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    oferta.titulo,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    oferta.descripcion,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Row(
                                    Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    IconButton (onClick = {
                                        viewModel.deleteOferta(oferta.id, empresaId)
                                    } )label@{
                                        Icon(
                                            Icons.Default.Delete,
                                            contentDescription = "Eliminar",
                                            tint = MaterialTheme.colorScheme.error
                                        )
                                    }
                                }
                            }
                        }
                    }
                    if (resp.data.isEmpty()) {
                        item {
                            Text(
                                "No tienes ofertas publicadas.",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
