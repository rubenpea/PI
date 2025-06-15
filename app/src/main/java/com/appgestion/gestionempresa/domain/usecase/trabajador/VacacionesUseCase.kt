package com.appgestion.gestionempresa.domain.usecase.trabajador

import com.appgestion.gestionempresa.domain.model.VacacionesEntity
import com.appgestion.gestionempresa.domain.repository.VacacionesRepository

class SubmitVacacionesUseCase(private val repo: VacacionesRepository) {
    suspend operator fun invoke(req: VacacionesEntity) = repo.submitRequest(req)
}

class GetMisVacacionesUseCase(private val repo: VacacionesRepository) {
    suspend operator fun invoke(workerId: String) = repo.getMyRequests(workerId)
}

class GetVacacionesPorEmpresaUseCase(private val repo: VacacionesRepository) {
    suspend operator fun invoke(empresaId: String) = repo.getRequestsByEmpresa(empresaId)
}

class UpdateVacacionesStatusUseCase(private val repo: VacacionesRepository) {
    suspend operator fun invoke(id: String, status: String) = repo.updateStatus(id, status)
}