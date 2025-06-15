package com.appgestion.gestionempresa.domain.model

data class FichajeEntity(
    val id: String = "",
    val uidUsuario: String,
    val fecha: String,
    val checkIn: Long,
    val checkOut: Long? = null,
    val duracion: Long? = null
)