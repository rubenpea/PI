package com.appgestion.gestionempresa.domain.model

data class EmpresaEntity(
    val id: String,
    val nombre: String,
    val descripcion: String = "",
    val fechaCreacion: Long,
    val listaTrabajadores: List<String> = emptyList()
)