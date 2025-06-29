package com.appgestion.gestionempresa.data.repository

import com.appgestion.gestionempresa.data.model.Response
import com.appgestion.gestionempresa.data.model.UsuarioDto
import com.appgestion.gestionempresa.data.model.toDomain
import com.appgestion.gestionempresa.data.model.toDto
import com.appgestion.gestionempresa.domain.model.UsuarioEntity
import com.appgestion.gestionempresa.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : AuthRepository {

    override suspend fun registerUser(usuario: UsuarioEntity, password: String): Response<Boolean> {
        return try {
            val result = auth.createUserWithEmailAndPassword(usuario.email, password).await()
            val userUid = result.user?.uid
                ?: return Response.Failure(Exception("UID no encontrado al crear usuario"))

            val usuarioConId = usuario.copy(id = userUid)
            val dto = usuarioConId.toDto(passwordEnTexto = "")

            firestore
                .collection("usuarios")
                .document(userUid)
                .set(dto)
                .await()

            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }


    override suspend fun loginUser(email: String, password: String): Response<UsuarioEntity> {

        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val uid = result.user?.uid
                ?: return Response.Failure(Exception("UID no encontrado al iniciar sesión"))

            val snapshot = firestore.collection("usuarios").document(uid).get().await()
            val dto = snapshot.toObject(UsuarioDto::class.java)
                ?: return Response.Failure(Exception("Usuario no encontrado en Firestore"))


            val usuarioDom = dto.toDomain()
            Response.Success(usuarioDom)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }



    override suspend fun fetchUser(uid: String): Response<UsuarioEntity> {
        return try {
            val snapshot = firestore.collection("usuarios").document(uid).get().await()
            val dto = snapshot.toObject(UsuarioDto::class.java)
                ?: return Response.Failure(Exception("Usuario no encontrado en Firestore"))
            val usuarioDom = dto.toDomain()
            Response.Success(usuarioDom)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }


    override suspend fun recuperarPassword() {
        TODO("Not yet implemented")
    }
}





