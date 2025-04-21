package com.appgestion.gestionempresa.ui.recuperarpass

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.appgestion.gestionempresa.ui.login.LoginViewModel

@Composable
fun RecuperarScreen(viewModel: LoginViewModel){

    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        OutlinedTextField(
            value = state.email,
            onValueChange = {viewModel.emailChange(it)},
            placeholder = { Text(text = "Indica email") },
            label = { Text(text = "Email") },
            modifier = Modifier
        )
    }
}