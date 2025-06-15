package com.appgestion.gestionempresa.ui.perfiles.empresa.facturas

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.appgestion.gestionempresa.data.model.Response
import com.appgestion.gestionempresa.domain.model.FacturaEntity
import coil.compose.AsyncImage
import java.util.Date

@Composable
fun FacturaListScreen(
    idEmpresa: String,
    viewModel: FacturaViewModel = hiltViewModel()
) {
    val facturasState by viewModel.listaFacturas.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.cargarFacturas(idEmpresa)
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Facturas subidas", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(16.dp))

        when (facturasState) {
            is Response.Failure -> {
                val msg = (facturasState as Response.Failure).exception.message
                if (msg == "Cargando") {
                    CircularProgressIndicator()
                } else {
                    Text("Error: $msg", color = Color.Red)
                }

            }
            is Response.Loading -> Response.Failure(Exception("Operación no válida en estado Loading"))

            is Response.Success -> {
                val lista = (facturasState as Response.Success<List<FacturaEntity>>).data
                if (lista.isEmpty()) {
                    Text("No hay facturas subidas.")
                } else {
                    LazyColumn {
                        items(lista) { factura ->
                            Column(modifier = Modifier.padding(8.dp)) {
                                AsyncImage(
                                    model = factura.url,
                                    contentDescription = "Factura",
                                    modifier = Modifier.height(200.dp).fillMaxWidth()
                                )
                                Text("Fecha: ${Date(factura.fecha)}", fontSize = 12.sp)
                                Divider()
                            }
                        }
                    }
                }
            }
        }
    }
}