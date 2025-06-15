package com.appgestion.gestionempresa.data.repository

import com.appgestion.gestionempresa.data.model.Response
import com.appgestion.gestionempresa.data.model.UsuarioDto
import com.appgestion.gestionempresa.data.model.toDomain
import com.appgestion.gestionempresa.domain.model.UsuarioEntity
import com.appgestion.gestionempresa.domain.repository.UserRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : UserRepository {
    override suspend fun getUserById(id: String): Response<UsuarioEntity> = try {
        val snap = firestore
            .collection("usuarios")
            .document(id)
            .get()
            .await()
        snap.toObject(UsuarioDto::class.java)
            ?.toDomain()
            ?.let { Response.Success(it) }
            ?: Response.Failure(Exception("Usuario no encontrado"))
    } catch (e: Exception) {
        Response.Failure(e)
    }
}
