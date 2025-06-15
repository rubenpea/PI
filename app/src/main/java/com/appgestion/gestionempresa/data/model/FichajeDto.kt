package com.appgestion.gestionempresa.data.model

import com.appgestion.gestionempresa.domain.model.FichajeEntity

data class FichajeDto(
    val uidUsuario: String = "",
    val fecha: String = "",
    val checkIn: Long = 0L,
    val checkOut: Long? = null,
    val duracion: Long? = null
)

fun FichajeDto.toDomain(id: String) = FichajeEntity(id, uidUsuario, fecha, checkIn, checkOut, duracion)
fun FichajeEntity.toDto() = FichajeDto(uidUsuario, fecha, checkIn, checkOut, duracion)