package com.appgestion.gestionempresa.domain.usecase.user

import com.appgestion.gestionempresa.data.model.Response
import com.appgestion.gestionempresa.domain.model.UsuarioEntity
import com.appgestion.gestionempresa.domain.repository.UserRepository
import javax.inject.Inject

class GetUserByIdUseCase @Inject constructor(
    private val repo: UserRepository
) {
    suspend operator fun invoke(userId: String): Response<UsuarioEntity> =
        repo.getUserById(userId)
}