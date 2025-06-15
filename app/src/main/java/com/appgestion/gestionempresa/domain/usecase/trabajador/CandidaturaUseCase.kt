package com.appgestion.gestionempresa.domain.usecase.trabajador

import com.appgestion.gestionempresa.data.model.Response
import com.appgestion.gestionempresa.domain.model.UsuarioEntity
import com.appgestion.gestionempresa.domain.repository.CandidaturaRepository
import javax.inject.Inject

class GetMisCandidaturasUseCase(private val repo: CandidaturaRepository) {
    suspend operator fun invoke(trabajadorId: String) =
        repo.getCandidaturasByTrabajador(trabajadorId)
}

class GetCandidaturasPorOfertaUseCase(private val repo: CandidaturaRepository) {
    suspend operator fun invoke(ofertaId: String) =
        repo.getCandidaturasByOferta(ofertaId)
}

// domain/usecase/empresa/UpdateCandidaturaStatusUseCase.kt
class UpdateCandidaturaStatusUseCase(private val repo: CandidaturaRepository) {
    suspend operator fun invoke(id: String, nuevoStatus: String) =
        repo.updateStatus(id, nuevoStatus)
}

class SendCandidaturaUseCase(
    private val repo: CandidaturaRepository
) {
    suspend operator fun invoke(
        trabajadorId: String,
        ofertaId: String,
        cvId: String
    ): Response<Boolean> = repo.sendCandidatura(trabajadorId, ofertaId, cvId)
}

class GetUsuarioByIdUseCase @Inject constructor(
    private val repo: CandidaturaRepository
) {
    suspend operator fun invoke(trabajadorId: String): Response<UsuarioEntity> =
        repo.getUsuarioById(trabajadorId)
}

