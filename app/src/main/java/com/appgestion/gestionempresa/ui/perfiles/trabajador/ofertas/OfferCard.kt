package com.appgestion.gestionempresa.ui.perfiles.trabajador.ofertas

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.appgestion.gestionempresa.domain.model.OfertaEntity
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OfertaCard(
    oferta: OfertaEntity,
    empresaNombre: String,
    onClick: () -> Unit
) {
    Card(
        modifier  = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp),
        onClick   = onClick,
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text     = empresaNombre,
                    style    = MaterialTheme.typography.titleSmall,
                    color    = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector        = Icons.Default.ArrowForward,
                    contentDescription = "Ver detalles",
                    modifier           = Modifier.size(20.dp)
                )
            }
            Text(text = oferta.titulo, style = MaterialTheme.typography.titleMedium)
            Text(
                text     = oferta.descripcion,
                maxLines = 2,
                style    = MaterialTheme.typography.bodyMedium
            )
            Text(
                text  = "Requisitos: ${oferta.requisitos}",
                style = MaterialTheme.typography.bodySmall
            )
            val fecha = Instant
                .ofEpochMilli(oferta.fechaPublicacion)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            Text(text = "Publicado: $fecha", style = MaterialTheme.typography.bodySmall)
        }
    }
}
