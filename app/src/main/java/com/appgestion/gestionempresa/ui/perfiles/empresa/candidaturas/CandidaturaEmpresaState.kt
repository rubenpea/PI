package com.appgestion.gestionempresa.ui.perfiles.empresa.candidaturas

import com.appgestion.gestionempresa.data.model.Response
import com.appgestion.gestionempresa.domain.model.CandidaturaEntity
import com.appgestion.gestionempresa.domain.model.UsuarioEntity

data class CandidaturasEmpresaState (
    val response: Response<List<CandidaturaEntity>> = Response.Loading,
        val usuarios: Map<String, UsuarioEntity>                  = emptyMap()
)