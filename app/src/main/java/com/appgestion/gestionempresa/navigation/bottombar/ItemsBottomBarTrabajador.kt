package com.appgestion.gestionempresa.navigation.bottombar

import com.appgestion.gestionempresa.R
import com.appgestion.gestionempresa.navigation.AppScreen

sealed class TrabajadorBottomItem(
    val icon: Int,
    val title: String,
    val route: String
) {
    object Tareas : TrabajadorBottomItem(
        icon = R.drawable.ic_person,
        title = "",
        route = AppScreen.HomeTrabajador.route
    )

    object Ofertas : TrabajadorBottomItem(
        icon = R.drawable.work,
        title = "",
        route = AppScreen.OfertasTrabajadorScreen.route
    )

    object Vacaciones : TrabajadorBottomItem(
        icon = R.drawable.ic_calendar,
        title = "",
        route = AppScreen.VacacionesScreen.route
    )

    object Fichaje : TrabajadorBottomItem(
        icon = R.drawable.ic_fichaje,
        title = "",
        route = AppScreen.FichajeScreen.route
    )

    object Candidaturas : TrabajadorBottomItem(
        icon = R.drawable.ic_candidaturas,
        title = "",
        route = AppScreen.MisCandidaturasScreen.route
    )
}