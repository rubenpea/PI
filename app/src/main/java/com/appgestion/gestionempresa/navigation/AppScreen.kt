package com.appgestion.gestionempresa.navigation

sealed class AppScreen(val route: String) {

    object LoginScreen: AppScreen("LoginScreen")
    object PantallaUno: AppScreen("PantallaUno")
    object RegistroScreen: AppScreen("RegistroScreen")
}