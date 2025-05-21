package com.appgestion.gestionempresa.domain.repository

import com.appgestion.gestionempresa.data.model.Response

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
  ): Response<Boolean>

  suspend fun recuperarPassword(
    email: String
  )
}