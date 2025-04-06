package com.appgestion.gestionempresa.ui.registro

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun RegistroScreen(navController: NavController){

    Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier,
            verticalArrangement = Arrangement.Center

        ) {
            OutlinedTextField(
                value = "",
                onValueChange = {},
                placeholder = { Text(text = "Login") },
                label = { Text(text = "Login") },
                modifier = Modifier
            )
            Spacer(
                modifier = Modifier
                    .padding(top = 8.dp)
            )

            OutlinedTextField(
                value = "",
                onValueChange = {},
                placeholder = { Text(text = "Password") },
                label = { Text(text = "Password") },
                modifier = Modifier

            )

            Spacer(
                modifier = Modifier
                    .padding(top = 8.dp)
            )

            OutlinedTextField(
                value = "",
                onValueChange = {},
                placeholder = { Text(text = "Password Repeat") },
                label = { Text(text = "Password Repeat") },
                modifier = Modifier
            )

            Spacer(
                modifier = Modifier
                    .padding(top = 8.dp)
            )

            OutlinedTextField(
                value = "",
                onValueChange = {},
                placeholder = { Text(text = "Nombre") },
                label = { Text(text = "Nombre") },
                modifier = Modifier
            )
            Spacer(
                modifier = Modifier
                    .padding(top = 8.dp)
            )

            OutlinedTextField(
                value = "",
                onValueChange = {},
                placeholder = { Text(text = "Teléfono") },
                label = { Text(text = "Teléfono") },
                modifier = Modifier
            )

        }
    }
}

@Preview
@Composable
fun preview(){
}