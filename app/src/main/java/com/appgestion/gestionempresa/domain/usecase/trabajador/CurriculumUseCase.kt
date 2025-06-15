package com.appgestion.gestionempresa.domain.usecase.trabajador

import com.appgestion.gestionempresa.domain.model.CurriculumEntity
import com.appgestion.gestionempresa.domain.repository.CurriculumRepository
import javax.inject.Inject

class CreateCVUseCase(private val repo: CurriculumRepository) {
    suspend operator fun invoke(cv: CurriculumEntity) = repo.createOrUpdateCV(cv)
}

class GetMyCVsUseCase(private val repo: CurriculumRepository) {
    suspend operator fun invoke(workerId: String) = repo.getMyCVs(workerId)
}

class GetCVByIdUseCase @Inject constructor(
    private val repo: CurriculumRepository
) {
    suspend operator fun invoke(cvId: String): CurriculumEntity? =
        repo.getCVById(cvId)
}

class SendCVUseCase(private val repo: CurriculumRepository) {
    suspend operator fun invoke(
        workerId: String, ofertaId: String, cvId: String
    ) = repo.sendCVToOffer(workerId, ofertaId, cvId)
}

