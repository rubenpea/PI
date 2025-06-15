package com.appgestion.gestionempresa.ui.perfiles.empresa.ofertas

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

import com.appgestion.gestionempresa.navigation.AppScreen
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.ui.text.input.KeyboardType
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter


@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OfertasScreen(
    empresaId: String,
    viewModel: OfertaViewModel = hiltViewModel(),
    onGoToMisOfertas: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    var openDatePicker by remember { mutableStateOf(false) }
    val dateState = rememberDatePickerState()

    val fechaCaducidadText = dateState.selectedDateMillis
        ?.let { ts ->
            Instant.ofEpochMilli(ts)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        }
        .orEmpty()

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Card(Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(2.dp)) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = state.titulo,
                    onValueChange = viewModel::onTituloChange,
                    label = { Text("Título") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = state.descripcion,
                    onValueChange = viewModel::onDescripcionChange,
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = state.categoria.orEmpty(),
                    onValueChange = viewModel::onCategoriaChange,
                    label = { Text("Categoría/Requisitos") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = state.salario.takeIf { it != 0.0 }?.toString() ?: "",
                    onValueChange = { v ->
                        v.toDoubleOrNull()?.let { viewModel.onSalarioChange(it) }
                    },
                    label = { Text("Salario") },
                    placeholder = { Text("e.g. 1200") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                OutlinedTextField(
                    value = fechaCaducidadText,
                    onValueChange = { },
                    readOnly = true,
                    label = { Text("Caducidad") },
                    trailingIcon = {
                        IconButton (onClick = { openDatePicker = true })
                        {
                            Icon(
                                imageVector = Icons.Default.CalendarToday,
                                contentDescription = "Seleccionar fecha"
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { openDatePicker = true }
                )

                Spacer(Modifier.height(8.dp))

                Button(
                    onClick = { viewModel.publicarOferta(empresaId) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = listOf(
                        state.titulo.isNotBlank(),
                        state.descripcion.isNotBlank(),
                        state.categoria?.isNotBlank() == true,
                        state.salario > 0.0,
                        state.fechaCaducidad != null
                    ).all { it }
                ) {
                    Text("Publicar Oferta")
                }

                OutlinedButton(
                    onClick = onGoToMisOfertas,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Ver mis Ofertas")
                }
            }
        }
    }

    if (openDatePicker) {
        DatePickerDialog(
            onDismissRequest = { openDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    dateState.selectedDateMillis?.let(viewModel::onCaducidadChange)
                    openDatePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { openDatePicker = false }) { Text("Cancelar") }
            }
        ) {
            DatePicker(state = dateState)
        }
    }
}