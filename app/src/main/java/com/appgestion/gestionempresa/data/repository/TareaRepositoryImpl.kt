package com.appgestion.gestionempresa.data.repository

import com.appgestion.gestionempresa.data.model.*
import com.appgestion.gestionempresa.domain.model.*
import com.appgestion.gestionempresa.domain.repository.TareaRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class TareaRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : TareaRepository {

    private val tareasCol = firestore.collection("tareas")

    override suspend fun crearTarea(tarea: TareaEntity): Response<Boolean> = try {
        tareasCol.add(tarea.toDto()).await()
        Response.Success(true)
    } catch (e: Exception) {
        Response.Failure(e)
    }

    override suspend fun obtenerTareasEmpresa(idEmpresa: String): Response<List<TareaEntity>> = try {
        val snapshot = tareasCol.whereEqualTo("empresaId", idEmpresa).get().await()
        val tareas = snapshot.documents.mapNotNull { it.toObject(TareaDto::class.java)?.toDomain() }
        Response.Success(tareas)
    } catch (e: Exception) {
        Response.Failure(e)
    }

    override suspend fun obtenerTareasTrabajador(idTrabajador: String): Response<List<TareaEntity>> = try {
        val snapshot = tareasCol.whereEqualTo("trabajadorId", idTrabajador).get().await()
        val tareas = snapshot.documents.mapNotNull { it.toObject(TareaDto::class.java)?.toDomain() }
        Response.Success(tareas)
    } catch (e: Exception) {
        Response.Failure(e)
    }

    override suspend fun actualizarEstadoTarea(idTarea: String, nuevoEstado: EstadoTarea): Response<Boolean> = try {
        tareasCol.document(idTarea).update("estado", nuevoEstado.name).await()
        Response.Success(true)
    } catch (e: Exception) {
        Response.Failure(e)
    }
}