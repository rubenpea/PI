package com.appgestion.gestionempresa.ui.perfiles.trabajador.cv

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.appgestion.gestionempresa.R

@Composable
fun CVFormScreen(
    navController: NavController,
    viewModel: CVFormViewModel = hiltViewModel()
) {
    val state      by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(
            painter           = painterResource(R.drawable.logo),
            contentDescription= "Logo Gestión",
            modifier          = Modifier.size(72.dp)
        )

        Text(
            text  = "Crear Curriculum",
            style = MaterialTheme.typography.titleLarge
        )

        // — Campos con validación —
        FieldWithError(
            value       = state.name,
            onValue     = viewModel::onNameChange,
            label       = "Nombre",
            placeholder = "Tu nombre completo",
            errorText   = state.nameError
        )
        FieldWithError(
            value       = state.email,
            onValue     = viewModel::onEmailChange,
            label       = "Email",
            placeholder = "usuario@correo.com",
            errorText   = state.emailError
        )
        FieldWithError(
            value       = state.phone,
            onValue     = viewModel::onPhoneChange,
            label       = "Teléfono",
            placeholder = "+34 600 000 000",
            errorText   = state.phoneError
        )
        FieldWithError(
            value       = state.summary,
            onValue     = viewModel::onSummaryChange,
            label       = "Resumen",
            placeholder = "Breve descripción de tu perfil",
            errorText   = state.summaryError,
            maxLines    = 4
        )
        FieldWithError(
            value       = state.skillsText,
            onValue     = viewModel::onSkillsChange,
            label       = "Habilidades (separadas por coma)",
            placeholder = "Kotlin, Compose, Firebase…",
            errorText   = state.skillsError
        )

        Button(
            onClick  = {
                viewModel.createCV()
                navController.popBackStack()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            enabled  = state.isValid
        ) {
            Text("Guardar CV")
        }
    }
}


@Composable
fun FieldWithError(
    value: String,
    onValue: (String) -> Unit,
    label: String,
    placeholder: String = "",
    errorText: String? = null,
    maxLines: Int = 1
) {
    Column {
        OutlinedTextField(
            value         = value,
            onValueChange = onValue,
            label         = { Text(label) },
            placeholder   = { Text(placeholder) },
            isError       = errorText != null,
            maxLines      = maxLines,
            modifier      = Modifier.fillMaxWidth()
        )
        errorText?.let {
            Text(
                text     = it,
                color    = MaterialTheme.colorScheme.error,
                style    = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 12.dp, top = 4.dp)
            )
        }
    }
}

