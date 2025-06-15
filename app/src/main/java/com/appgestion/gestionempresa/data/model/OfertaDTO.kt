package com.appgestion.gestionempresa.data.model

import com.appgestion.gestionempresa.domain.model.OfertaEntity

data class OfertaDto(
    val id: String = "",
    val idEmpresa: String = "",
    val titulo: String = "",
    val descripcion: String = "",
    val salario: Double = 0.0,
    val requisitos: String = "",
    val fechaPublicacion: Long = 0L,
    val fechaCaducidad: Long? = 0L
)

fun OfertaDto.toDomain(): OfertaEntity = OfertaEntity(id, idEmpresa, titulo, descripcion, salario, requisitos, fechaPublicacion, fechaCaducidad)
fun OfertaEntity.toDto(): OfertaDto = OfertaDto(id, idEmpresa, titulo, descripcion, salario, requisitos, fechaPublicacion, fechaCaducidad)
