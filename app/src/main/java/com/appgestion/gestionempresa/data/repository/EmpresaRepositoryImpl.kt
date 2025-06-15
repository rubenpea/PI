package com.appgestion.gestionempresa.data.repository

import com.appgestion.gestionempresa.data.model.EmpresaDto
import com.appgestion.gestionempresa.data.model.Response
import com.appgestion.gestionempresa.data.model.UsuarioDto
import com.appgestion.gestionempresa.data.model.toDomain
import com.appgestion.gestionempresa.data.model.toDto
import com.appgestion.gestionempresa.domain.model.EmpresaEntity
import com.appgestion.gestionempresa.domain.model.UsuarioEntity
import com.appgestion.gestionempresa.domain.repository.EmpresaRepository
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class EmpresaRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : EmpresaRepository {

    private val empresasCol = firestore.collection("empresas")

    override suspend fun saveEmpresa(empresa: EmpresaEntity): Response<Boolean> {
        return try {
            val dto = empresa.toDto()
            empresasCol.document(empresa.id).set(dto).await()
            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    override suspend fun fetchEmpresa(uid: String): Response<EmpresaEntity> {
        return try {
            val snap = empresasCol.document(uid).get().await()
            val dto = snap.toObject(EmpresaDto::class.java)
                ?: return Response.Failure(Exception("Empresa no encontrada"))
            Response.Success(dto.toDomain())
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    override suspend fun findTrabajadorIdByEmail(email: String): Response<String> {
        return try {
            val snapshot = firestore.collection("usuarios")
                .whereEqualTo("email", email)
                .limit(1)
                .get()
                .await()

            if (snapshot.isEmpty) {
                Response.Failure(Exception("Usuario no registrado"))
            } else {
                val uid = snapshot.documents.first().id
                Response.Success(uid)
            }
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    override suspend fun addTrabajadorToEmpresa(
        idEmpresa: String,
        uidUsuario: String
    ): Response<Unit> {
        return try {
            empresasCol.document(idEmpresa)
                .update("listaTrabajadores", FieldValue.arrayUnion(uidUsuario))
                .await()
            Response.Success(Unit)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    override suspend fun removeTrabajador(empUid: String, trabUid: String): Response<Boolean> {
        return try {
            empresasCol.document(empUid)
                .update("listaTrabajadores", FieldValue.arrayRemove(trabUid))
                .await()
            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    override suspend fun obtenerTrabajadoresDeEmpresa(empresaId: String): List<UsuarioEntity> {
        val snapshot = firestore.collection("usuarios")
            .whereEqualTo("tipo", "trabajador")
            .whereEqualTo("empresaId", empresaId)
            .get().await()

        return snapshot.documents.mapNotNull { it.toObject(UsuarioDto::class.java)?.toDomain() }
    }

    override suspend fun asignarEmpresaAlTrabajador(trabajadorId: String, empresaId: String): Response<Unit> {
        return try {
            firestore.collection("usuarios")
                .document(trabajadorId)
                .update("empresaId", empresaId)
                .await()
            Response.Success(Unit)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    override suspend fun getEmpresaById(id: String): Response<EmpresaEntity> = try {
        val snap = firestore.collection("empresas")
            .document(id)
            .get().await()
            .toObject(EmpresaDto::class.java)
            ?.toDomain()
        if (snap != null) Response.Success(snap)
        else            Response.Failure(Exception("Empresa no encontrada"))
    } catch (e: Exception) {
        Response.Failure(e)
    }
}