package com.appgestion.gestionempresa.domain.repository

import com.appgestion.gestionempresa.data.model.Response
import com.appgestion.gestionempresa.domain.model.UsuarioEntity

interface AuthRepository {

  suspend fun registerUser(usuario: UsuarioEntity, password: String): Response<Boolean>

  suspend fun loginUser(
    email: String,
    password: String

  ): Response<UsuarioEntity>

  suspend fun fetchUser(uid: String):Response<UsuarioEntity>



 suspend fun recuperarPassword()


}