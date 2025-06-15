package com.appgestion.gestionempresa.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.appgestion.gestionempresa.domain.model.Role
import com.appgestion.gestionempresa.navigation.AppScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScaffold(
    navController: NavController,
    userRole: Role?,
    bottomBar: @Composable () -> Unit,
    topBarActions: @Composable RowScope.() -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    val navBack   by navController.currentBackStackEntryAsState()
    val current   = navBack?.destination?.route
    val screen    = AppScreen.all.find { it.route == current }
    val canGoBack = navController.previousBackStackEntry != null

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(screen?.title.orEmpty()) },
                navigationIcon = {
                    if (canGoBack) {
                        IconButton (onClick = { navController.navigateUp() })
                        { Icon(Icons.Default.ArrowBack, contentDescription = "Atrás") }
                    }
                },
                actions = topBarActions
            )
        },
        bottomBar = {
            if (screen?.showBottomBar == true) {
                bottomBar()
            }
        },
        content = content
    )
}