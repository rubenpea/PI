package com.appgestion.gestionempresa.domain.model

data class CandidaturaEntity(
    val id: String = "",
    val trabajadorId: String,
    val ofertaId: String,
    val cvId: String,
    val timestamp: Long,
    val status: String = "PENDIENTE"  // PENDIENTE, ACEPTADA, RECHAZADA
)