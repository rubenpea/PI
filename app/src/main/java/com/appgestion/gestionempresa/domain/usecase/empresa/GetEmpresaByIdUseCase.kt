package com.appgestion.gestionempresa.domain.usecase.empresa

import com.appgestion.gestionempresa.data.model.Response
import com.appgestion.gestionempresa.domain.model.EmpresaEntity
import com.appgestion.gestionempresa.domain.repository.EmpresaRepository
import javax.inject.Inject

class GetEmpresaByIdUseCase @Inject constructor(
    private val repo: EmpresaRepository
) {
    suspend operator fun invoke(id: String): Response<EmpresaEntity> =
        repo.getEmpresaById(id)
}