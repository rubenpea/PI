package com.appgestion.gestionempresa.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.appgestion.gestionempresa.ui.login.LoginScreen
import androidx.navigation.compose.composable
import com.appgestion.gestionempresa.ui.login.LoginViewModel
import com.appgestion.gestionempresa.ui.perfiles.empresa.HomeScreenEmpresa
import com.appgestion.gestionempresa.ui.perfiles.trabajador.HomeScreen
import com.appgestion.gestionempresa.ui.recuperarpass.RecuperarScreen
import com.appgestion.gestionempresa.ui.registro.ElegirRegistro
import com.appgestion.gestionempresa.ui.registro.empresa.RegistroEmpresaScreen
import com.appgestion.gestionempresa.ui.registro.empresa.RegistroEmpresaViewModel
import com.appgestion.gestionempresa.ui.registro.trabajador.RegistroTrabajadorScreen
import com.appgestion.gestionempresa.ui.registro.trabajador.RegistroTrabajadorViewModel


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
        composable(route = AppScreen.HomeTrabajador.route){
            HomeScreen()
        }
        composable(route = AppScreen.HomeEmpresa.route){
            HomeScreenEmpresa()
        }
    }

}


