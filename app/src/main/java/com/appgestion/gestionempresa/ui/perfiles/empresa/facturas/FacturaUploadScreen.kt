package com.appgestion.gestionempresa.ui.perfiles.empresa.facturas

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.appgestion.gestionempresa.data.model.Response

@Composable
fun FacturaUploadScreen(
    idEmpresa: String,
    viewModel: FacturaViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { viewModel.subirFactura(it, idEmpresa) }
    }

    val uploadState by viewModel.uidState.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        Button(onClick = { launcher.launch("image/*") }) {
            Text("Seleccionar Imagen de Factura")
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (uploadState) {
            is Response.Failure -> {
                val message = (uploadState as Response.Failure).exception.message ?: ""
                if (message == "Cargando") CircularProgressIndicator()
                else Text("❌ Error: $message", color = Color.Red)
            }
            is Response.Success -> {
                val url = (uploadState as Response.Success).data
                if (url.isNotBlank()) {
                    Text("✅ Subida con éxito")
                    Text("URL: $url", fontSize = 12.sp)
                }
            }
            is Response.Loading -> Response.Failure(Exception("Operación no válida en estado Loading"))
        }
    }
}
