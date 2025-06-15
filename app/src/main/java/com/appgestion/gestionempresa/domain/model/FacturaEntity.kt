package com.appgestion.gestionempresa.domain.model

data class FacturaEntity(
    val id: String = "",
    val empresaId: String,
    val url: String,
    val fecha: Long,
)