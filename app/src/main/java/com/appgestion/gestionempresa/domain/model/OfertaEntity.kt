package com.appgestion.gestionempresa.domain.model

data class OfertaEntity(
    val id: String = "",
    val idEmpresa: String = "",
    val titulo: String = "",
    val descripcion: String = "",
    val salario: Double = 0.0,
    val requisitos: String = "",
    val fechaPublicacion: Long = System.currentTimeMillis(),
    val fechaCaducidad: Long? = null
)