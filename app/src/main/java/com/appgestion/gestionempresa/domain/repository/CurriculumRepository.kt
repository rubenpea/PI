package com.appgestion.gestionempresa.domain.repository

import com.appgestion.gestionempresa.data.model.Response
import com.appgestion.gestionempresa.domain.model.CurriculumEntity

interface CurriculumRepository {
    suspend fun createOrUpdateCV(cv: CurriculumEntity): Response<Boolean>
    suspend fun getMyCVs(workerId: String): Response<List<CurriculumEntity>>
    suspend fun sendCVToOffer(
        workerId: String,
        ofertaId: String,
        cvId: String
    ): Response<Boolean>

    suspend fun getCVById(cvId: String): CurriculumEntity?
}