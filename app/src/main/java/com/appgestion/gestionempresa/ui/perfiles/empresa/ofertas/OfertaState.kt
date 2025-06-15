package com.appgestion.gestionempresa.ui.perfiles.empresa.ofertas

import com.appgestion.gestionempresa.data.model.Response
import com.appgestion.gestionempresa.domain.model.OfertaEntity
import java.util.Date
data class OfertaState(
    // — campos del formulario —
    val titulo: String = "",
    val descripcion: String = "",
    val categoria: String? = null,
    val fechaCaducidad: Long? = 0L,
    val salario: Double = 0.0,
    // — Aquí guardamos el Response con la lista de ofertas —
    val ofertasResponse: Response<List<OfertaEntity>> = Response.Loading
)