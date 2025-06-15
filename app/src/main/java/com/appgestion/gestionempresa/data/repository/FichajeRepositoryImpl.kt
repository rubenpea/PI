package com.appgestion.gestionempresa.data.repository

import com.appgestion.gestionempresa.data.model.FichajeDto
import com.appgestion.gestionempresa.data.model.Response
import com.appgestion.gestionempresa.data.model.toDomain
import com.appgestion.gestionempresa.data.model.toDto
import com.appgestion.gestionempresa.domain.model.FichajeEntity
import com.appgestion.gestionempresa.domain.repository.FichajeRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FichajeRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : FichajeRepository {

    private val fichajesCol = firestore.collection("fichajes")

    override suspend fun iniciarFichaje(fichaje: FichajeEntity): Response<Boolean> = try {
        fichajesCol.add(fichaje.toDto()).await()
        Response.Success(true)
    } catch (e: Exception) {
        Response.Failure(e)
    }

    override suspend fun obtenerFichajesUsuario(uid: String): Response<List<FichajeEntity>> = try {
        val snapshot = fichajesCol
            .whereEqualTo("uidUsuario", uid)
            .get().await()

        val lista = snapshot.documents.mapNotNull {
            it.toObject(FichajeDto::class.java)?.toDomain(it.id)
        }
        Response.Success(lista)
    } catch (e: Exception) {
        Response.Failure(e)
    }

    override suspend fun finalizarFichaje(fichajeId: String, checkOutTime: Long): Response<Boolean> {
        return try {
            val docRef = fichajesCol.document(fichajeId)
            val snapshot = docRef.get().await()
            val dto = snapshot.toObject(FichajeDto::class.java) ?: return@finalizarFichaje Response.Failure(Exception("Fichaje no encontrado"))

            val duracion = checkOutTime - dto.checkIn

            docRef.update(
                mapOf(
                    "checkOut" to checkOutTime,
                    "duracion" to duracion
                )
            ).await()

            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }
}