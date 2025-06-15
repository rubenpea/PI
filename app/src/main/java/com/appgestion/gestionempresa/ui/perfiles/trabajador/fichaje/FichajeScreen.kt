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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.appgestion.gestionempresa.data.model.Response
import com.appgestion.gestionempresa.domain.model.FichajeEntity
import com.appgestion.gestionempresa.navigation.AppScreen
import com.appgestion.gestionempresa.ui.components.Dimens
import com.appgestion.gestionempresa.ui.perfiles.trabajador.fichaje.FichajeViewModel
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

@Composable
fun FichajeScreen(
    viewModel: FichajeViewModel = hiltViewModel(),
    navController: NavController
) {
    val uid = FirebaseAuth.getInstance().currentUser?.uid.orEmpty()
    val registro by viewModel.registro.collectAsState()
    val historial by viewModel.historial.collectAsState()
    val fichajeAbierto by viewModel.fichajeAbierto.collectAsState()

    LaunchedEffect(uid) {
        if (uid.isNotEmpty()) viewModel.cargarHistorial(uid)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = Dimens.padding)
    ) {
        Spacer(Modifier.height(Dimens.smallPadding))

        // Botón iniciar/finalizar
        Button(
            onClick = {
                if (fichajeAbierto == null) viewModel.iniciarFichaje(uid)
                else viewModel.finalizarFichaje(uid)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text(
                text = if (fichajeAbierto == null) "Iniciar fichaje" else "Finalizar fichaje",
                style = MaterialTheme.typography.labelLarge
            )
        }

        Spacer(Modifier.height(Dimens.padding))

        // Mensaje de resultado
        when (registro) {
            is Response.Loading -> CircularProgressIndicator()
            is Response.Success -> {
                if ((registro as Response.Success).data) {
                    Text(
                        text = "✅ Fichaje registrado correctamente",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            is Response.Failure -> {
                Text(
                    text = "❌ Error: ${(registro as Response.Failure).exception.message}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        Spacer(Modifier.height(Dimens.padding))
        Divider()
        Spacer(Modifier.height(Dimens.smallPadding))

        Text(
            text = "Historial",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(start = Dimens.padding)
        )

        Spacer(Modifier.height(Dimens.smallPadding))

        when (historial) {
            is Response.Loading -> Box(
                Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) { CircularProgressIndicator() }

            is Response.Success -> {
                val lista = (historial as Response.Success).data
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(Dimens.smallPadding),
                    contentPadding = PaddingValues(bottom = Dimens.padding)
                ) {
                    items(lista.sortedByDescending { it.fecha }) { fichaje ->
                        FichajeRow(fichaje = fichaje)
                    }
                }
            }

            else -> Text(
                text = "No hay datos aún.",
                modifier = Modifier.padding(Dimens.padding),
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(Modifier.weight(1f))

        // Botón resumen semanal
        Button(
            onClick = { navController.navigate(AppScreen.ResumenSemanalScreen.route) },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text("Ver resumen semanal")
        }

        Spacer(Modifier.height(Dimens.padding))
    }
}

@Composable
private fun FichajeRow(fichaje: FichajeEntity) {
    val sdf = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }

    val epochMillis = fichaje.fecha.toLongOrNull() ?: 0L
    val date = Date(epochMillis)

    val horas = fichaje.duracion?.let { dur ->
        val totalHours = dur / 1000f / 60f / 60f
        val hrs = totalHours.toInt()
        val mins = ((totalHours % 1) * 60).roundToInt()
        "%dh %02dm".format(hrs, mins)
    } ?: "--"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.padding),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(Dimens.padding),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.CalendarToday, contentDescription = null)
            Spacer(Modifier.width(Dimens.smallPadding))
            Text(
                text = sdf.format(date),
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(Modifier.weight(1f))
            Text(
                text = horas,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

