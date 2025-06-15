package com.appgestion.gestionempresa.ui.empresa

import android.os.Build
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.appgestion.gestionempresa.R
import com.appgestion.gestionempresa.data.model.Response
import com.appgestion.gestionempresa.domain.model.EstadoTarea
import com.appgestion.gestionempresa.domain.model.FacturaEntity
import com.appgestion.gestionempresa.domain.model.TareaEntity
import com.appgestion.gestionempresa.domain.model.VacacionesEntity
import com.appgestion.gestionempresa.navigation.AppScreen
import com.appgestion.gestionempresa.ui.components.AppCard
import com.appgestion.gestionempresa.ui.perfiles.empresa.EmpresaViewModel
import com.appgestion.gestionempresa.ui.perfiles.empresa.facturas.FacturaViewModel
import com.appgestion.gestionempresa.ui.perfiles.empresa.tareas.TareaViewModel
import com.appgestion.gestionempresa.ui.perfiles.empresa.vacaciones.VacacionesEmpresaViewModel
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.ExperimentalFoundationApi
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalFoundationApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EmpresaScreen(
    navController: NavController,
    viewModel: EmpresaViewModel                  = hiltViewModel(),
    tareaViewModel: TareaViewModel               = hiltViewModel(),
    vacacionesViewModel: VacacionesEmpresaViewModel = hiltViewModel(),
    facturaViewModel: FacturaViewModel           = hiltViewModel()
) {
    val uiStateEmpresa by viewModel.uiState.collectAsState()
    val trabajadores   by viewModel.trabajadoresInfo.collectAsState()
    val tareasState    by tareaViewModel.tareas.collectAsState()
    val vacState       by vacacionesViewModel.uiState.collectAsState()
    val facturasState  by facturaViewModel.listaFacturas.collectAsState()

    val empresaId = FirebaseAuth.getInstance().currentUser?.uid.orEmpty()
    val currentUser = FirebaseAuth.getInstance().currentUser
    val ownerEmail = currentUser?.email.orEmpty()
    var ownerName by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(empresaId) {
        if (empresaId.isBlank()) return@LaunchedEffect

        viewModel.fetchEmpresa(empresaId)
        viewModel.cargarTrabajadoresVinculados(empresaId)
        tareaViewModel.cargarTareasEmpresa(empresaId)
        vacacionesViewModel.loadRequests(empresaId)
        facturaViewModel.cargarFacturas(empresaId)

        currentUser?.uid?.let { uid ->
            val snap = FirebaseFirestore.getInstance()
                .collection("usuarios")
                .document(uid)
                .get()
                .await()
            ownerName = snap.getString("name")
        }
    }


    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        AppCard(Modifier.fillMaxWidth()) {
            Column(
                Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    "Datos Empresa",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.height(8.dp))

                Text(
                    uiStateEmpresa.empresa?.nombre.orEmpty(),
                    style = MaterialTheme.typography.headlineSmall
                )

                if (ownerEmail.isNotBlank()) {
                    ownerName?.let {
                        Text(
                            "Propietario"+it,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    Text(
                        "Email"+ownerEmail,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(Modifier.height(12.dp))

                Text(
                    uiStateEmpresa.empresa?.descripcion.orEmpty(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Text("Resumen", style = MaterialTheme.typography.titleMedium)

        val pendT = (tareasState as? Response.Success<List<TareaEntity>>)
            ?.data?.count { it.estado == EstadoTarea.PENDIENTE } ?: 0
        val pendV = (vacState.response as? Response.Success<List<VacacionesEntity>>)
            ?.data?.count { it.status == "PENDIENTE" } ?: 0
        val factC = (facturasState as? Response.Success<List<FacturaEntity>>)
            ?.data?.size ?: 0

        // Grid 2×2
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding     = PaddingValues(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement   = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        ) {
            item {
                ProfileStat(
                    iconRes = R.drawable.ic_group,
                    label   = "Trabajadores",
                    count   = trabajadores.size
                ) {
                    navController.navigate(AppScreen.EmpresaTrabajadoresScreen.route)
                }
            }
            item {
                ProfileStat(
                    iconRes = R.drawable.task,
                    label   = "Tareas\npendientes",
                    count   = pendT
                ) {
                    navController.navigate(AppScreen.TareasEmpresaScreen.createRoute(empresaId))
                }
            }
            item {
                ProfileStat(
                    iconRes = R.drawable.ic_calendar,
                    label   = "Vacaciones\npendientes",
                    count   = pendV
                ) {
                    navController.navigate(AppScreen.VacacionesEmpresaScreen.createRoute(empresaId))
                }
            }
            item {
                ProfileStat(
                    iconRes = R.drawable.ic_invoice,
                    label   = "Facturas\nsubidas",
                    count   = factC
                ) {
                    navController.navigate(AppScreen.FacturaListScreen.createRoute(empresaId))
                }
            }
        }

        if (uiStateEmpresa.loading) {
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        uiStateEmpresa.error?.let { msg ->
            Text(
                msg,
                color    = MaterialTheme.colorScheme.error,
                modifier = Modifier.fillMaxWidth().wrapContentWidth(Alignment.CenterHorizontally)
            )
        }
        uiStateEmpresa.successMessage?.let { msg ->
            Text(
                msg,
                color    = MaterialTheme.colorScheme.primary,
                modifier = Modifier.fillMaxWidth().wrapContentWidth(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
fun ProfileStat(
    @DrawableRes iconRes: Int,
    label: String,
    count: Int,
    onClick: () -> Unit
) {
    Card(
        modifier  = Modifier
            .size(120.dp)
            .clickable(onClick = onClick),
        shape     = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement  = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter             = painterResource(iconRes),
                contentDescription  = null,
                modifier            = Modifier.size(32.dp)
            )
            Spacer(Modifier.height(4.dp))
            Text(count.toString(), style = MaterialTheme.typography.titleLarge)
            Text(label, style = MaterialTheme.typography.bodySmall, textAlign = TextAlign.Center)
        }
    }
}




