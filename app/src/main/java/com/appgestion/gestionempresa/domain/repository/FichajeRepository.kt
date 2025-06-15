package com.appgestion.gestionempresa.domain.repository

import com.appgestion.gestionempresa.data.model.Response
import com.appgestion.gestionempresa.domain.model.FichajeEntity

interface FichajeRepository {
    suspend fun iniciarFichaje(fichaje: FichajeEntity): Response<Boolean>
    suspend fun obtenerFichajesUsuario(uid: String): Response<List<FichajeEntity>>
    suspend fun finalizarFichaje(fichajeId: String, checkOutTime: Long): Response<Boolean>
}