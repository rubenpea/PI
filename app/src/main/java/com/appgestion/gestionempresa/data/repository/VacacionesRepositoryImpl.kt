package com.appgestion.gestionempresa.data.repository

import android.util.Log
import com.appgestion.gestionempresa.data.model.Response
import com.appgestion.gestionempresa.data.model.VacacionesDto
import com.appgestion.gestionempresa.data.model.toDomain
import com.appgestion.gestionempresa.data.model.toDto
import com.appgestion.gestionempresa.domain.model.VacacionesEntity
import com.appgestion.gestionempresa.domain.repository.VacacionesRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class VacacionesRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
): VacacionesRepository {
    private val col = firestore.collection("vacaciones")

    override suspend fun submitRequest(req: VacacionesEntity) = try {
        val doc = if (req.id.isEmpty()) col.document() else col.document(req.id)
        val dto = req.copy(id = doc.id).toDto()
        doc.set(dto).await()
        Response.Success(true)
    } catch(e: Exception) { Response.Failure(e) }

    override suspend fun getMyRequests(workerId: String) = try {
        val snap = col.whereEqualTo("workerId", workerId).get().await()
        val list = snap.documents.mapNotNull {
            it.toObject(VacacionesDto::class.java)?.toDomain()
        }
        Response.Success(list)
    } catch(e: Exception) { Response.Failure(e) }

    override suspend fun getRequestsByEmpresa(empresaId: String) = try {
        Log.d("VacacionesRepo", "buscando vacaciones para empresaId = $empresaId")
        val snap = col.whereEqualTo("empresaId", empresaId).get().await()
        val list = snap.documents.mapNotNull {
            it.toObject(VacacionesDto::class.java)?.toDomain()
        }
        Response.Success(list)
    } catch(e: Exception) { Response.Failure(e) }

    override suspend fun updateStatus(id: String, newStatus: String) = try {
        col.document(id).update("status", newStatus).await()
        Response.Success(true)
    } catch(e: Exception) { Response.Failure(e) }
}
