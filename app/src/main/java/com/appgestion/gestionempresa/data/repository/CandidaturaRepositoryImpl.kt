package com.appgestion.gestionempresa.data.repository

import com.appgestion.gestionempresa.data.model.CandidaturaDto
import com.appgestion.gestionempresa.data.model.Response
import com.appgestion.gestionempresa.data.model.toDomain
import com.appgestion.gestionempresa.domain.model.UsuarioEntity
import com.appgestion.gestionempresa.domain.repository.CandidaturaRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class CandidaturaRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : CandidaturaRepository {
    private val col = firestore.collection("candidaturas")

    override suspend fun getCandidaturasByTrabajador(trabajadorId: String) = try {
        val snap = col.whereEqualTo("trabajadorId", trabajadorId).get().await()
        Response.Success(snap.documents.mapNotNull {
            it.toObject(CandidaturaDto::class.java)?.toDomain()
        })
    } catch(e: Exception) { Response.Failure(e) }

    override suspend fun getCandidaturasByOferta(ofertaId: String) = try {
        val snap = col.whereEqualTo("ofertaId", ofertaId).get().await()
        Response.Success(snap.documents.mapNotNull {
            it.toObject(CandidaturaDto::class.java)?.toDomain()
        })
    } catch(e: Exception) { Response.Failure(e) }

    override suspend fun updateStatus(candidaturaId: String, status: String) = try {
        col.document(candidaturaId).update("status", status).await()
        Response.Success(true)
    } catch(e: Exception) { Response.Failure(e) }


    override suspend fun sendCandidatura(
        trabajadorId: String,
        ofertaId: String,
        cvId: String
    ): Response<Boolean> = try {
        val doc = col.document()
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
        override suspend fun getUsuarioById(id: String): Response<UsuarioEntity> {
            return try {
                val doc = firestore.collection("usuarios")
                    .document(id)
                    .get()
                    .await()
                val user = doc.toObject(UsuarioEntity::class.java)
                if (user != null) {
                    Response.Success(user)
                } else {
                    Response.Failure(Exception("Usuario no encontrado"))
                }
            } catch (e: Exception) {
                Response.Failure(e)
            }
        }


}
