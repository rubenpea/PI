package com.appgestion.gestionempresa.domain.repository

interface EmpresaRepository {

    suspend fun addTrabajador(
        name: String,
        apellidos: String,
        email: String,
        number: String,
    )

    suspend fun publicarOferta(
        name: String,
        descripcion: String,

    )
}