package com.appgestion.gestionempresa.ui.perfiles.empresa.vacaciones

import com.appgestion.gestionempresa.data.model.Response
import com.appgestion.gestionempresa.domain.model.VacacionesEntity

data class VacacionesEmpresaState (
    val response: Response<List<VacacionesEntity>> = Response.Loading
)