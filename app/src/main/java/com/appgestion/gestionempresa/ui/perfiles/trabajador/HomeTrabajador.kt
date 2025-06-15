package com.appgestion.gestionempresa.ui.perfiles.trabajador

import android.annotation.SuppressLint
import androidx.annotation.DrawableRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.appgestion.gestionempresa.R
import com.appgestion.gestionempresa.data.model.Response
import com.appgestion.gestionempresa.domain.model.CurriculumEntity
import com.appgestion.gestionempresa.domain.model.EstadoTarea
import com.appgestion.gestionempresa.domain.model.TareaEntity
import com.appgestion.gestionempresa.navigation.AppScreen
import com.appgestion.gestionempresa.ui.components.Dimens
import com.appgestion.gestionempresa.ui.perfiles.empresa.tareas.TareaViewModel
import com.appgestion.gestionempresa.ui.perfiles.trabajador.cv.CurriculumViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreenTrabajador(
    navController: NavController,
    tareaViewModel: TareaViewModel       = hiltViewModel(),
    cvViewModel: CurriculumViewModel     = hiltViewModel()
) {
    val user   = FirebaseAuth.getInstance().currentUser
    val uid    = user?.uid.orEmpty()
    val email  = user?.email.orEmpty()

    // Estado local para el nombre
    var nombre by remember { mutableStateOf("Usuario") }

    // Carga nombre y tareas al iniciar
    LaunchedEffect(uid) {
        if (uid.isNotEmpty()) {
            val snap = FirebaseFirestore.getInstance()
                .collection("usuarios")
                .document(uid)
                .get()
                .await()
            nombre = snap.getString("name")
                ?: snap.getString("nombre")
                        ?: email.substringBefore('@')
        }
        tareaViewModel.cargarTareasTrabajador(uid)
    }

    // Conteo de pendientes
    val tareaState by tareaViewModel.tareas.collectAsState()
    val pendientes = (tareaState as? Response.Success<List<TareaEntity>>)
        ?.data?.count { it.estado == EstadoTarea.PENDIENTE }
        ?: 0

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = Dimens.padding),
        verticalArrangement = Arrangement.spacedBy(Dimens.padding)
    ) {
        Spacer(Modifier.height(Dimens.smallPadding))

        // Tarjeta usuario + tareas pendientes
        UserTasksCard(
            name       = nombre,
            email      = email,
            pendientes = pendientes,
            onEdit     = {
                // por ejemplo, ir a perfil
                navController.navigate(AppScreen.Profile.route)
            }
        )

        Spacer(Modifier.height(Dimens.padding))

        // Tarjeta CV
        CVHomeCard(
            navController = navController,
            cvViewModel   = cvViewModel
        )

        Spacer(Modifier.height(Dimens.padding))

        // Opciones: Tareas / Fichar
        Row(
            Modifier
                .fillMaxWidth()
                .height(140.dp),
            horizontalArrangement = Arrangement.spacedBy(Dimens.padding)
        ) {
            ProfileStat(
                iconRes  = R.drawable.task,
                label    = "Tareas",
                count    = null,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                navController.navigate(AppScreen.TareasTrabajadorScreen.route)
            }

            ProfileStat(
                iconRes  = R.drawable.ic_fichaje,
                label    = "Fichar",
                count    = null,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                navController.navigate(AppScreen.FichajeScreen.route)
            }
        }
    }
}

// ----- Componentes auxiliares -----

@Composable
fun UserTasksCard(
    name: String,
    email: String,
    pendientes: Int,
    onEdit: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 120.dp)
            .clickable(onClick = onEdit),
        shape     = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(Modifier.padding(Dimens.padding)) {
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector   = Icons.Default.Person,
                    contentDescription = "Usuario",
                    modifier = Modifier.size(28.dp)
                )
                Spacer(Modifier.width(Dimens.smallPadding))
                Text(
                    text     = "Hola, $name",
                    style    = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = onEdit) {
                    Icon(
                        imageVector   = Icons.Default.Edit,
                        contentDescription = "Editar perfil"
                    )
                }
            }
            Spacer(Modifier.height(Dimens.smallPadding))
            Text(email, style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(Dimens.smallPadding))
            Text(
                text  = "Tareas pendientes: $pendientes",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun CVHomeCard(
    navController: NavController,
    cvViewModel: CurriculumViewModel = hiltViewModel()
) {
    val state by cvViewModel.uiState.collectAsState()
    LaunchedEffect(Unit) {
        FirebaseAuth.getInstance().currentUser?.uid
            ?.let { cvViewModel.loadMyCVs(it) }
    }
    val cv = (state as? Response.Success<CurriculumEntity>)?.data

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 120.dp)
            .clickable { navController.navigate(AppScreen.CVFormScreen.route) },
        shape     = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(Modifier.padding(Dimens.padding)) {
            if (cv != null) {
                Column {
                    Row(
                        Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text     = "Tu CV",
                            style    = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.weight(1f)
                        )
                        Icon(
                            imageVector   = Icons.Default.Edit,
                            contentDescription = "Editar CV"
                        )
                    }
                    Spacer(Modifier.height(Dimens.smallPadding))
                    Text(cv.name,  style = MaterialTheme.typography.bodyLarge)
                    Text(cv.email, style = MaterialTheme.typography.bodyMedium)
                    Text(cv.phone, style = MaterialTheme.typography.bodyMedium)
                    Spacer(Modifier.height(Dimens.smallPadding))
                    Text(
                        text  = "Habilidades: ${cv.skills.joinToString()}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            } else {
                Column(
                    Modifier.fillMaxSize(),
                    verticalArrangement  = Arrangement.Center,
                    horizontalAlignment  = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector   = Icons.Default.Add,
                        contentDescription = "Crear CV",
                        modifier     = Modifier.size(36.dp)
                    )
                    Spacer(Modifier.height(Dimens.smallPadding))
                    Text(
                        text  = "Crear CV",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}

@Composable
fun ProfileStat(
    @DrawableRes iconRes: Int,
    label: String,
    count: Int? = null,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        onClick   = onClick,
        modifier  = modifier,
        shape     = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(Dimens.padding),
            verticalArrangement  = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter            = painterResource(iconRes),
                contentDescription = label,
                modifier           = Modifier.size(32.dp)
            )
            Spacer(Modifier.height(Dimens.smallPadding))
            count?.let {
                Text(
                    text  = it.toString(),
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(Modifier.height(4.dp))
            }
            Text(
                text      = label,
                style     = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}
