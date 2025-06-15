package com.appgestion.gestionempresa.domain.repository

import com.appgestion.gestionempresa.data.model.Response
import com.appgestion.gestionempresa.domain.model.UsuarioEntity

interface UserRepository {

    suspend fun getUserById(id: String): Response<UsuarioEntity>
}