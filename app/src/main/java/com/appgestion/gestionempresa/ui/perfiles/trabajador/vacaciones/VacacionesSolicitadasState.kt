package com.appgestion.gestionempresa.ui.perfiles.trabajador.vacaciones

import com.appgestion.gestionempresa.data.model.Response
import com.appgestion.gestionempresa.domain.model.VacacionesEntity

data class VacacionesSolicitadasState (
    val response: Response<List<VacacionesEntity>> = Response.Loading
)