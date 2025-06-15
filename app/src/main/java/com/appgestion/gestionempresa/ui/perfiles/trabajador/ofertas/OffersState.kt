package com.appgestion.gestionempresa.ui.perfiles.trabajador.ofertas


import com.appgestion.gestionempresa.data.model.Response
import com.appgestion.gestionempresa.domain.model.OfertaEntity

data class OffersState(
    val response: Response<List<OfertaEntity>> = Response.Loading
)
