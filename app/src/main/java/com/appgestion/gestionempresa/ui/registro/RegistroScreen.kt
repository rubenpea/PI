package com.appgestion.gestionempresa.ui.registro

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.appgestion.gestionempresa.navigation.AppNavigation
import com.appgestion.gestionempresa.navigation.AppScreen

@Composable
fun ElegirRegistro(navController: NavController){

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { navController.navigate(AppScreen.RegistroEmpresaScreen.route)}
            ) {
                Text(text = "Empresa")
            }
            Button(
                onClick = { navController.navigate(AppScreen.RegistroTrabajadorScreen.route) }
            ) {
                Text(text = "Trabajador")
            }
        }
    }
}