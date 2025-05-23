package com.appgestion.gestionempresa.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.appgestion.gestionempresa.ui.login.LoginScreen
import androidx.navigation.compose.composable
import com.appgestion.gestionempresa.ui.perfiles.empresa.HomeEmpresaScreen
import com.appgestion.gestionempresa.ui.perfiles.trabajador.HomeScreenTrabajador
import com.appgestion.gestionempresa.ui.recuperarpass.RecuperarScreen
import com.appgestion.gestionempresa.ui.registro.ElegirRegistro
import com.appgestion.gestionempresa.ui.registro.empresa.RegistroEmpresaScreen
import com.appgestion.gestionempresa.ui.registro.trabajador.RegistroTrabajadorScreen


@Composable
fun AppNavigation(){

   val navController = rememberNavController()

    NavHost(navController = navController, startDestination = AppScreen.LoginScreen.route) {
        composable(route = AppScreen.LoginScreen.route) {
            LoginScreen(navController = navController, viewModel = hiltViewModel())
        }
        composable(route = AppScreen.RegistroScreen.route){
            ElegirRegistro(navController = navController)
        }
        composable(route = AppScreen.RegistroEmpresaScreen.route){
            RegistroEmpresaScreen(navController = navController, viewModel = hiltViewModel())
        }
        composable(route = AppScreen.RegistroTrabajadorScreen.route){
            RegistroTrabajadorScreen(navController = navController, viewModel = hiltViewModel())
        }
        composable(route = AppScreen.RecupararScreen.route){
            RecuperarScreen( viewModel = hiltViewModel())
        }
        composable(route = AppScreen.HomeEmpresa.route){
            HomeEmpresaScreen(navController)
        }
        composable(route = AppScreen.HomeTrabajador.route){
            HomeScreenTrabajador()
        }
    }

}


