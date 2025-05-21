package com.appgestion.gestionempresa.domain.repository

interface TrabajadorRepository {

    suspend fun verOfertasDisponibles(

    )

    suspend fun solicitarVacaciones(

    )
}