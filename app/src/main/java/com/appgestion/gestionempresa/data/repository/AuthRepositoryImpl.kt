package com.appgestion.gestionempresa.data.repository

import com.appgestion.gestionempresa.data.model.Response
import com.appgestion.gestionempresa.data.model.Usuarios
import com.appgestion.gestionempresa.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : AuthRepository {

    override suspend fun registerUser(
        email: String,
        password: String,
        tipe: String
    ): Response<Boolean> {

        return try {

            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val userUid =
                result?.user?.uid ?: return Response.Failure(Exception("Uid no encontrado"))

            val usuario = Usuarios(
                uid = userUid,
                email = email,
                tipo = tipe
            )
            firestore.collection(tipe).document(userUid).set(usuario).await()
            return Response.Success(true)
        } catch (e: Exception) {
            return Response.Failure(e)
        }
    }
}
