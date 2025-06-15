package com.appgestion.gestionempresa.data.model

import com.appgestion.gestionempresa.domain.model.VacacionesEntity

data class VacacionesDto(
    val id: String = "",
    val workerId: String = "",
    val empresaId: String  = "",
    val startDate: Long = 0L,
    val endDate: Long = 0L,
    val days: Int = 0,
    val status: String = "PENDIENTE"
)

fun VacacionesDto.toDomain() = VacacionesEntity(
    id        = id,
    workerId  = workerId,
    empresaId  = empresaId,
    startDate = startDate,
    endDate   = endDate,
    days      = days,
    status    = status
)

fun VacacionesEntity.toDto() = VacacionesDto(
    id        = id,
    workerId  = workerId,
    empresaId  = empresaId,
    startDate = startDate,
    endDate   = endDate,
    days      = days,
    status    = status
)
