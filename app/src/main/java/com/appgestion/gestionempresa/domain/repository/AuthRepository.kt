package com.appgestion.gestionempresa.domain.repository

import com.appgestion.gestionempresa.data.model.Response
import com.appgestion.gestionempresa.data.model.Usuarios

interface AuthRepository {

  suspend fun registerUser(
    email: String,
    password: String,
    tipe: String,
    name: String,
    phone: String
  ): Response<Boolean>

  suspend fun loginUser(
    email: String,
    password: String
  ): Response<Usuarios>


  suspend fun recuperarPassword(
    email: String
  )


}