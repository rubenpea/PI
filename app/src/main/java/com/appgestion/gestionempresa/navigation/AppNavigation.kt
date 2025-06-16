package com.appgestion.gestionempresa.navigation

import FichajeScreen
import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.appgestion.gestionempresa.ui.login.LoginScreen
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.appgestion.gestionempresa.domain.model.Role
import com.appgestion.gestionempresa.navigation.bottombar.BottomBarEmpresa
import com.appgestion.gestionempresa.navigation.bottombar.BottomBarTrabajador
import com.appgestion.gestionempresa.ui.components.AppScaffold
import com.appgestion.gestionempresa.ui.components.SplashScreen
import com.appgestion.gestionempresa.ui.empresa.EmpresaScreen
import com.appgestion.gestionempresa.ui.perfiles.empresa.EmpresaTrabajadoresScreen
import com.appgestion.gestionempresa.ui.perfiles.empresa.ProfileScreen
import com.appgestion.gestionempresa.ui.perfiles.empresa.SetupEmpresaScreen
import com.appgestion.gestionempresa.ui.perfiles.empresa.candidaturas.CandidaturasScreen
import com.appgestion.gestionempresa.ui.perfiles.empresa.candidaturas.CvDetailScreen
import com.appgestion.gestionempresa.ui.perfiles.empresa.facturas.FacturaListScreen
import com.appgestion.gestionempresa.ui.perfiles.empresa.facturas.FacturaUploadScreen
import com.appgestion.gestionempresa.ui.perfiles.empresa.facturas.FacturasScreen
import com.appgestion.gestionempresa.ui.perfiles.empresa.ofertas.MisOfertasScreen
import com.appgestion.gestionempresa.ui.perfiles.empresa.ofertas.OfertasScreen
import com.appgestion.gestionempresa.ui.perfiles.empresa.tareas.AsignarTareaScreen
import com.appgestion.gestionempresa.ui.perfiles.empresa.vacaciones.VacacionesEmpresaScreen

import com.appgestion.gestionempresa.ui.perfiles.trabajador.HomeScreenTrabajador
import com.appgestion.gestionempresa.ui.perfiles.trabajador.candidaturas.CandidaturasTrabajadorScreen
import com.appgestion.gestionempresa.ui.perfiles.trabajador.cv.CVFormScreen
import com.appgestion.gestionempresa.ui.perfiles.trabajador.fichaje.ResumenSemanalScreen
import com.appgestion.gestionempresa.ui.perfiles.trabajador.ofertas.OfferDetailScreen
import com.appgestion.gestionempresa.ui.perfiles.trabajador.ofertas.OffersScreen
import com.appgestion.gestionempresa.ui.perfiles.trabajador.tareas.TareasTrabajadorScreen
import com.appgestion.gestionempresa.ui.perfiles.trabajador.vacaciones.VacacionesScreen
import com.appgestion.gestionempresa.ui.perfiles.trabajador.vacaciones.VacacionesSolicitadasScreen
import com.appgestion.gestionempresa.ui.recuperarpass.RecuperarScreen
import com.appgestion.gestionempresa.ui.registro.empresa.RegistroEmpresaScreen
import com.appgestion.gestionempresa.ui.registro.trabajador.RegistroTrabajadorScreen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

@SuppressLint("SuspiciousIndentation")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val user = FirebaseAuth.getInstance().currentUser
    var role by remember { mutableStateOf<Role?>(null) }
    val isLoggedIn = user != null

    val navBack by navController.currentBackStackEntryAsState()
    val currentRoute = navBack?.destination?.route

            // Carga rol si ya está logueado
            LaunchedEffect(isLoggedIn) {
                if (isLoggedIn) {
                    user!!.uid.let { uid ->
                        FirebaseFirestore.getInstance()
                            .collection("usuarios")
                            .document(uid)
                            .get().await()
                            .getString("tipo")
                            ?.let { tipo ->
                                role = if (tipo == "empresa") Role.EMPRESA else Role.TRABAJADOR
                            }
                    }
                }
            }

            AppScaffold(
                navController = navController,
                userRole = role,
                bottomBar = {
                    when (role) {
                        Role.EMPRESA -> BottomBarEmpresa(navController)
                        Role.TRABAJADOR -> BottomBarTrabajador(navController)
                        else -> {}
                    }
                },
                topBarActions = {
                    if (currentRoute == AppScreen.HomeEmpresa.route) {
                        IconButton(onClick = { navController.navigate(AppScreen.Profile.route) }) {
                            Icon(Icons.Default.Person, contentDescription = "Perfil")
                        }
                    }
                    if (currentRoute == AppScreen.HomeTrabajador.route) {
                        IconButton(onClick = {
                            FirebaseAuth.getInstance().signOut()
                            navController.navigate(AppScreen.LoginScreen.route) {
                                popUpTo(navController.graph.startDestinationId) { inclusive = true }
                            }
                        }) {
                            Icon(Icons.Default.ExitToApp, contentDescription = "Cerrar sesión")
                        }
                    }
                }
            ) { padding ->
                NavHost(
                    navController = navController,
                    startDestination = AppScreen.SplashScreen.route,
                    modifier = Modifier.padding(padding)
                ) {
                    composable(AppScreen.SplashScreen.route) {
                        SplashScreen(
                            navController = navController,
                            isLoggedIn = isLoggedIn,
                            roleLoaded = role != null,
                            getNextRoute = {
                                when {
                                    !isLoggedIn -> AppScreen.LoginScreen.route
                                    role == Role.EMPRESA -> AppScreen.HomeEmpresa.route
                                    role == Role.TRABAJADOR -> AppScreen.HomeTrabajador.route
                                    else -> AppScreen.LoginScreen.route
                                }
                            },
                            splashDelay = 2500L
                        )
                    }
                    // --- AUTENTICACIÓN ---
                    composable(AppScreen.LoginScreen.route) {
                        LoginScreen(navController = navController, viewModel = hiltViewModel())
                    }
                    composable(AppScreen.RegistroEmpresaScreen.route) {
                        RegistroEmpresaScreen(
                            navController = navController,
                            viewModel = hiltViewModel()
                        )
                    }
                    composable(AppScreen.SetupEmpresaScreen.route) {
                        SetupEmpresaScreen(
                            navController = navController,
                            viewModel = hiltViewModel()
                        )
                    }
                    composable(AppScreen.RegistroTrabajadorScreen.route) {
                        RegistroTrabajadorScreen(
                            navController = navController,
                            viewModel = hiltViewModel()
                        )
                    }
                    composable(AppScreen.RecupararScreen.route) {
                        RecuperarScreen(viewModel = hiltViewModel())
                    }

                    // --- FLOJO EMPRESA ---
                    composable(AppScreen.HomeEmpresa.route) {
                        EmpresaScreen(navController = navController, viewModel = hiltViewModel())
                    }

                    composable(AppScreen.Profile.route) {
                        ProfileScreen(navController)
                    }

                    composable(AppScreen.EmpresaTrabajadoresScreen.route) {
                        EmpresaTrabajadoresScreen(
                            navController = navController,
                            viewModel = hiltViewModel()
                        )
                    }

                    composable(
                        route = AppScreen.OfertaScreen.route,
                        arguments = listOf(navArgument("empresaId") {
                            type = NavType.StringType
                        })
                    ) { back ->
                        val empresaId = back.arguments!!.getString("empresaId")!!
                        OfertasScreen(
                            empresaId = empresaId,
                            viewModel = hiltViewModel(),
                            onGoToMisOfertas = {
                                navController.navigate(
                                    AppScreen.MisOfertasEmpresaScreen.createRoute(
                                        empresaId
                                    )
                                )
                            }
                        )
                    }


                    composable(
                        route = AppScreen.MisOfertasEmpresaScreen.route,
                        arguments = listOf(navArgument("empresaId") {
                            type = NavType.StringType
                        })
                    ) { back ->
                        val empresaId = back.arguments!!.getString("empresaId")!!
                        MisOfertasScreen(
                            empresaId = empresaId,
                            navController = navController,
                            viewModel = hiltViewModel(),
                            onVerDetalle = { ofertaId ->
                                navController.navigate(
                                    AppScreen.CandidaturasOfertaScreen.createRoute(
                                        ofertaId
                                    )
                                )
                            }
                        )
                    }
                    composable(
                        route = AppScreen.CandidaturasOfertaScreen.route,
                        arguments = listOf(navArgument("ofertaId") { type = NavType.StringType })
                    ) { back ->
                        val ofertaId = back.arguments!!.getString("ofertaId")!!
                        CandidaturasScreen(ofertaId, navController)
                    }
                    composable(
                        route = AppScreen.CvDetailScreen.route,
                        arguments = listOf(navArgument("cvId") { type = NavType.StringType })
                    ) { back ->
                        val cvId = back.arguments!!.getString("cvId")!!
                        CvDetailScreen(cvId = cvId, navController = navController)
                    }
                    composable(
                        route = AppScreen.FacturaScreen.route,
                        arguments = listOf(navArgument("idEmpresa") { type = NavType.StringType })
                    ) { back ->
                        FacturaUploadScreen(back.arguments!!.getString("idEmpresa")!!)
                    }
                    composable(
                        route = AppScreen.FacturaListScreen.route,
                        arguments = listOf(navArgument("idEmpresa") { type = NavType.StringType })
                    ) { back ->
                        FacturaListScreen(back.arguments!!.getString("idEmpresa")!!)
                    }

                    composable(
                        route = AppScreen.FacturaListScreenNav.route,
                        arguments = listOf(navArgument("idEmpresa") {
                            type = NavType.StringType
                        })
                    ) { back ->
                        val idEmpresa = back.arguments!!.getString("idEmpresa")!!
                        FacturasScreen(
                            idEmpresa = idEmpresa,
                            viewModel = hiltViewModel()
                        )
                    }

                    composable(
                        route = AppScreen.FacturaUploadScreenNav.route,
                        arguments = listOf(navArgument("idEmpresa") {
                            type = NavType.StringType
                        })
                    ) { back ->
                        val idEmpresa = back.arguments!!.getString("idEmpresa")!!
                        FacturaUploadScreen(idEmpresa = idEmpresa)
                    }


                    composable(
                        route = AppScreen.TareasEmpresaScreen.route,
                        arguments = listOf(navArgument("idEmpresa") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val empresaId = backStackEntry.arguments!!.getString("idEmpresa")!!
                        AsignarTareaScreen(
                            empresaId = empresaId,
                            navController = navController,
                            empresaViewModel = hiltViewModel(),
                            tareaViewModel = hiltViewModel()
                        )
                    }
                    composable(
                        route = AppScreen.VacacionesEmpresaScreen.route,
                        arguments = listOf(navArgument("empresaId") { type = NavType.StringType })
                    ) { back ->
                        VacacionesEmpresaScreen(
                            empresaId = back.arguments!!.getString("empresaId")!!,
                            navController = navController,
                            viewModel = hiltViewModel()
                        )
                    }

                    // --- FLOJO TRABAJADOR ---
                    composable(AppScreen.HomeTrabajador.route) {
                        HomeScreenTrabajador(navController)
                    }
                    composable("ofertas/{empresaId}") { backStack ->
                        val empresaId = backStack.arguments!!.getString("empresaId")!!
                        OfertasScreen(
                            empresaId = empresaId,
                            onGoToMisOfertas = {
                                navController.navigate("misOfertas/$empresaId")
                            }
                        )
                    }

                    composable(
                        route = AppScreen.OfertasTrabajadorScreen.route,

                        ) {
                        OffersScreen(navController = navController)
                    }
                    composable(
                        route = AppScreen.OfertaDetailScreen.route,
                        arguments = listOf(navArgument("ofertaId") { type = NavType.StringType })
                    ) { back ->
                        OfferDetailScreen(
                            ofertaId = back.arguments!!.getString("ofertaId")!!,
                            navController = navController,
                        )
                    }
                    composable(AppScreen.CVFormScreen.route) {
                        CVFormScreen(navController = navController)
                    }
                    composable(AppScreen.MisCandidaturasScreen.route) {
                        CandidaturasTrabajadorScreen(

                            viewModel = hiltViewModel(),
                            navController = navController
                        )
                    }
                    composable(AppScreen.TareasTrabajadorScreen.route) {
                        TareasTrabajadorScreen(viewModel = hiltViewModel())
                    }
                    composable(AppScreen.VacacionesScreen.route) {
                        VacacionesScreen(
                            vacacionesViewModel = hiltViewModel(),
                            vacacionesSolicitadasViewModel = hiltViewModel(),
                            navController = navController
                        )
                    }
                    composable(AppScreen.VacacionesSolicitadasScreen.route) {
                        VacacionesSolicitadasScreen(
                            viewModel = hiltViewModel(),
                            navController = navController
                        )
                    }
                    composable(AppScreen.FichajeScreen.route) {
                        FichajeScreen(viewModel = hiltViewModel(), navController = navController)
                    }
                    composable(AppScreen.ResumenSemanalScreen.route) {
                        ResumenSemanalScreen(viewModel = hiltViewModel())
                    }
                }
            }
        }

