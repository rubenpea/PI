package com.appgestion.gestionempresa.navigation.bottombar

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomBarTrabajador(navController: NavController) {
    val items = listOf(
        TrabajadorBottomItem.Tareas,
        TrabajadorBottomItem.Vacaciones,
        TrabajadorBottomItem.Fichaje,
        TrabajadorBottomItem.Ofertas,
        TrabajadorBottomItem.Candidaturas   // ← Nueva línea
    )
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                icon = { Icon(painterResource(id = item.icon), contentDescription = null) },
                label = { Text(item.title) },
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationRoute!!) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}