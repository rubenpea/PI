package com.appgestion.gestionempresa.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.appgestion.gestionempresa.R
import com.appgestion.gestionempresa.navigation.AppScreen
import kotlinx.coroutines.delay



@Composable
fun SplashScreen(
    navController: NavHostController,
    isLoggedIn: Boolean,
    roleLoaded: Boolean,
    getNextRoute: () -> String,
    splashDelay: Long = 2500L,
    animDuration: Int = 800
) {
    var startAnim by remember { mutableStateOf(false) }
    val animSpec = tween<Float>(animDuration, easing = FastOutSlowInEasing)
    val scale by animateFloatAsState(if (startAnim) 1f else 0.6f, animationSpec = animSpec)
    val alpha by animateFloatAsState(if (startAnim) 1f else 0f, animationSpec = animSpec)

    LaunchedEffect(Unit) {
        startAnim = true
        delay(splashDelay)

        if (isLoggedIn) {
            val timeout = System.currentTimeMillis() + 1000L
            while (!roleLoaded && System.currentTimeMillis() < timeout) {
                delay(100)
            }
        }

        navController.navigate(getNextRoute()) {
            popUpTo(AppScreen.SplashScreen.route) { inclusive = true }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter            = painterResource(R.drawable.logo),
            contentDescription = "Logo Splash",
            modifier           = Modifier
                .size(120.dp)
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    this.alpha = alpha
                }
        )
    }
}