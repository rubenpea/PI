package com.appgestion.gestionempresa.data.repository

import com.appgestion.gestionempresa.data.model.*
import com.appgestion.gestionempresa.domain.model.CurriculumEntity
import com.appgestion.gestionempresa.domain.repository.CurriculumRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class CurriculumRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
): CurriculumRepository {
    private val cvsCol = firestore.collection("curriculum")
    private val candidaturasCol = firestore.collection("candidaturas")

    override suspend fun createOrUpdateCV(cv: CurriculumEntity): Response<Boolean> =
        try {
            val doc = if (cv.id.isEmpty()) cvsCol.document() else cvsCol.document(cv.id)
            val dto = cv.copy(id = doc.id).toDto()
            doc.set(dto).await()
            Response.Success(true)
        } catch (e: Exception) { Response.Failure(e) }

    override suspend fun getMyCVs(workerId: String): Response<List<CurriculumEntity>> =
        try {
            val snap = cvsCol.whereEqualTo("workerId", workerId).get().await()
            val list = snap.documents.mapNotNull { it.toObject(CurriculumDto::class.java)?.toDomain() }
            Response.Success(list)
        } catch (e: Exception) { Response.Failure(e) }

    override suspend fun sendCVToOffer(
        trabajadorId: String,
        ofertaId: String,
        cvId: String
    ): Response<Boolean> = try {
        val doc = candidaturasCol.document()
        val dto = CandidaturaDto(
            id           = doc.id,
            trabajadorId = trabajadorId,
            ofertaId     = ofertaId,
            cvId         = cvId,
            timestamp    = System.currentTimeMillis(),
            status       = "PENDIENTE"
        )
        doc.set(dto).await()
        Response.Success(true)
    } catch(e: Exception) {
        Response.Failure(e)
    }


    override suspend fun getCVById(cvId: String): CurriculumEntity? = try {
        val snap = firestore.collection("curriculum").document(cvId).get().await()
        snap.toObject(CurriculumDto::class.java)?.toDomain()
    } catch (e: Exception) {
        null
    }
}

