package com.appgestion.gestionempresa.ui.perfiles.trabajador.candidaturas

import com.appgestion.gestionempresa.data.model.Response
import com.appgestion.gestionempresa.domain.model.CandidaturaEntity

data class CandidaturasTrabajadorState(
    val response: Response<List<CandidaturaEntity>> = Response.Loading
)