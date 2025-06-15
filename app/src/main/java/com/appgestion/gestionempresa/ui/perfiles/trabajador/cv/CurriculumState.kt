package com.appgestion.gestionempresa.ui.perfiles.trabajador.cv

import com.appgestion.gestionempresa.data.model.Response
import com.appgestion.gestionempresa.domain.model.CurriculumEntity

data class CurriculumState (
    val response: Response<List<CurriculumEntity>> = Response.Loading
    )