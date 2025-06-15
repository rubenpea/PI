package com.appgestion.gestionempresa.data.model

import com.appgestion.gestionempresa.domain.model.CandidaturaEntity

data class CandidaturaDto(
    val id: String = "",
    val trabajadorId: String = "",
    val ofertaId: String = "",
    val cvId: String = "",
    val timestamp: Long = 0L,
    val status: String = "PENDIENTE"
)

fun CandidaturaDto.toDomain() = CandidaturaEntity(
    id            = id,
    trabajadorId  = trabajadorId,
    ofertaId      = ofertaId,
    cvId          = cvId,
    timestamp     = timestamp,
    status        = status
)

fun CandidaturaEntity.toDto() = CandidaturaDto(
    id            = id,
    trabajadorId  = trabajadorId,
    ofertaId      = ofertaId,
    cvId          = cvId,
    timestamp     = timestamp,
    status        = status
)