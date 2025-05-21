package com.appgestion.gestionempresa.navigation

sealed class AppScreen(val route: String) {

    object LoginScreen: AppScreen("login_screen")
    object RegistroScreen: AppScreen("registro_screen")
    object RegistroEmpresaScreen: AppScreen("registro_empresa_screen")
    object RegistroTrabajadorScreen: AppScreen("registro_trabajador_screen")
    object RecupararScreen: AppScreen("recuperar_screen")
    object HomeTrabajador: AppScreen("hom_trabajador")
    object HomeEmpresa: AppScreen("hom_empresa")
}