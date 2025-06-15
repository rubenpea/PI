package com.appgestion.gestionempresa.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.appgestion.gestionempresa.domain.model.OfertaEntity
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OfertaCard(
    oferta: OfertaEntity,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(oferta.titulo, style = MaterialTheme.typography.titleMedium)
            Text(oferta.descripcion, style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 4.dp))
            Text("Requisitos: ${oferta.requisitos}",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 8.dp))
            val fecha = Instant.ofEpochMilli(oferta.fechaPublicacion)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            Text("Publicado: $fecha",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 8.dp))
        }
    }
}
