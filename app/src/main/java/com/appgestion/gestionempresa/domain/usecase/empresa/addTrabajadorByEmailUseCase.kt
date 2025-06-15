package com.appgestion.gestionempresa.domain.usecase.empresa

import com.appgestion.gestionempresa.data.model.Response
import com.appgestion.gestionempresa.domain.repository.EmpresaRepository

class AddTrabajadorByEmailUseCase(
    private val empresaRepository: EmpresaRepository
) {
    suspend operator fun invoke(
        idEmpresa: String,
        email: String
    ): Response<Unit> {
        val result = empresaRepository.findTrabajadorIdByEmail(email)
        return when (result) {
            is Response.Success -> {
                val trabajadorId = result.data

                val addResponse = empresaRepository.addTrabajadorToEmpresa(idEmpresa, trabajadorId)

                if (addResponse is Response.Success) {
                    empresaRepository.asignarEmpresaAlTrabajador(trabajadorId, idEmpresa)
                } else {
                    addResponse
                }
            }

            is Response.Failure -> Response.Failure(result.exception)
            is Response.Loading -> Response.Failure(Exception("Operación no válida en estado Loading"))
        }
    }
}