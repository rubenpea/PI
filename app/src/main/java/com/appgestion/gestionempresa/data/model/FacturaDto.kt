package com.appgestion.gestionempresa.data.model

import com.appgestion.gestionempresa.domain.model.FacturaEntity

data class FacturaDto(
    val empresaId: String = "",
    val url: String = "",
    val timestamp: Long = 0L
)

fun FacturaDto.toDomain(id: String): FacturaEntity {
    return FacturaEntity(
        id = id,
        empresaId = empresaId,
        url = url,
        fecha = timestamp
    )
}

fun FacturaEntity.toDto(): FacturaDto {
    return FacturaDto(
        empresaId = empresaId,
        url = url,
        timestamp = fecha
    )
}