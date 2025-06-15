package com.appgestion.gestionempresa.data.repository

import com.appgestion.gestionempresa.data.model.OfertaDto
import com.appgestion.gestionempresa.data.model.Response
import com.appgestion.gestionempresa.data.model.toDomain
import com.appgestion.gestionempresa.data.model.toDto
import com.appgestion.gestionempresa.domain.model.OfertaEntity
import com.appgestion.gestionempresa.domain.repository.OfertaRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class OfertaRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : OfertaRepository {

    private val ofertasCol = firestore.collection("ofertas")

    override suspend fun crearOferta(oferta: OfertaEntity): Response<Boolean> {
        return try {
            val doc = ofertasCol.document()
            val dto = oferta.copy(id = doc.id).toDto()
            doc.set(dto).await()
            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    override suspend fun eliminarOferta(ofertaId: String): Response<Boolean> = try {
        ofertasCol.document(ofertaId).delete().await()
        Response.Success(true)
    } catch (e: Exception) {
        Response.Failure(e)
    }

    override suspend fun obtenerOfertasPorEmpresa(empresaId: String): Response<List<OfertaEntity>> {
        return try {
            val snapshot = ofertasCol
                .whereEqualTo("idEmpresa", empresaId)
                .get().await()
            val lista =
                snapshot.documents.mapNotNull { it.toObject(OfertaDto::class.java)?.toDomain() }
            Response.Success(lista)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    override suspend fun getAllOfertas(): Response<List<OfertaEntity>> = try {
        val snapshot = firestore.collection("ofertas")
            .get().await()
        val lista = snapshot.documents
            .mapNotNull { it.toObject(OfertaDto::class.java)?.toDomain() }
        Response.Success(lista)
    } catch (e: Exception) {
        Response.Failure(e)
    }

    override suspend fun getOfertaById(id: String): Response<OfertaEntity> = try {
        val snap = firestore.collection("ofertas")
            .document(id)
            .get()
            .await()
            .toObject(OfertaDto::class.java)
            ?.toDomain()
        if (snap != null) Response.Success(snap)
        else Response.Failure(Exception("Oferta no encontrada"))
    } catch (e: Exception) {
        Response.Failure(e)
    }



}

