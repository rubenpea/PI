package com.appgestion.gestionempresa.navigation.bottombar

import androidx.annotation.DrawableRes
import com.appgestion.gestionempresa.R
import com.appgestion.gestionempresa.navigation.AppScreen
import com.google.firebase.auth.FirebaseAuth

sealed class EmpresaTab(
    @DrawableRes val icon: Int,
    val title: String,
    val route: String
) {
    object Perfil : EmpresaTab(
        icon  = R.drawable.ic_person,
        title = "",
        route = AppScreen.HomeEmpresa.route
    )

    object Trabajadores : EmpresaTab(
        icon  = R.drawable.ic_group,
        title = "",
        route = AppScreen.EmpresaTrabajadoresScreen.route
    )

    object Vacaciones : EmpresaTab(
        icon  = R.drawable.ic_calendar,
        title = "",
        route = AppScreen.VacacionesEmpresaScreen.createRoute(
            FirebaseAuth.getInstance().currentUser?.uid ?: ""
        )
    )

    object Facturas : EmpresaTab(
        icon  = R.drawable.ic_invoice,
        title = "",
        route = AppScreen.FacturaListScreen.createRoute(
            FirebaseAuth.getInstance().currentUser?.uid ?: ""
        )
    )

    object Ofertas: EmpresaTab(
        icon = R.drawable.work,
        title = "",
        route = AppScreen.OfertaScreen.createRoute(
            FirebaseAuth.getInstance().currentUser?.uid ?: ""
        )
    )
}