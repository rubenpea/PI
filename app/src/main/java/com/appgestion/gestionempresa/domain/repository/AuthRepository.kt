package com.appgestion.gestionempresa.domain.repository

import com.appgestion.gestionempresa.data.model.Response
interface AuthRepository {

  suspend fun registerUser(email: String, password: String, tipe: String): Response<Boolean>

}