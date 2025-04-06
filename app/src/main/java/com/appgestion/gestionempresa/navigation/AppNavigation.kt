package com.appgestion.gestionempresa.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHost
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.appgestion.gestionempresa.ui.login.LoginScreen
import androidx.navigation.compose.composable
import com.appgestion.gestionempresa.ui.registro.RegistroScreen


@Composable
fun AppNavigation(){

   val navController = rememberNavController()

    NavHost(navController = navController, startDestination = AppScreen.LoginScreen.route) {
        composable(route = AppScreen.LoginScreen.route) {
            LoginScreen(navController = navController)
        }
        composable(route = AppScreen.RegistroScreen.route){
            RegistroScreen(navController = navController)
        }
    }

}


