package com.appgestion.gestionempresa.domain.model

data class VacacionesEntity(
    val id: String = "",
    val workerId: String,
    val empresaId: String,
    val startDate: Long,
    val endDate: Long,
    val days: Int,
    val status: String = "PENDIENTE" // PENDIENTE, ACEPTADA, RECHAZADA
)