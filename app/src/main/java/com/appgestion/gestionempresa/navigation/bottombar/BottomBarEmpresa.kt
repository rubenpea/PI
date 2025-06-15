package com.appgestion.gestionempresa.navigation.bottombar

import android.annotation.SuppressLint
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun BottomBarEmpresa(navController: NavController) {
    val items = listOf(
        EmpresaTab.Perfil,
        EmpresaTab.Trabajadores,
        EmpresaTab.Vacaciones,
        EmpresaTab.Facturas,
        EmpresaTab.Ofertas
    )

    NavigationBar {
        val backStack by navController.currentBackStackEntryAsState()
        val currentRoute = backStack?.destination?.route

        items.forEach { tab ->
            NavigationBarItem(
                selected   = currentRoute == tab.route,
                icon       = { Icon(painterResource(tab.icon), contentDescription = null) },
                label      = { Text(tab.title) },
                onClick    = {
                    navController.navigate(tab.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState   = true
                    }
                }
            )
        }
    }
}