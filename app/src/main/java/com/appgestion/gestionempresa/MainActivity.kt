package com.appgestion.gestionempresa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.appgestion.gestionempresa.navigation.AppNavigation
import com.appgestion.gestionempresa.ui.theme.GestionEmpresaPITheme



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GestionEmpresaPITheme {
                AppNavigation()
            }
        }
    }
}


