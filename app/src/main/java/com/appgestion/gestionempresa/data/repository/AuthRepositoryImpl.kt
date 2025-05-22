package com.appgestion.gestionempresa.data.repository

import android.util.Log
import com.appgestion.gestionempresa.data.model.Response
import com.appgestion.gestionempresa.data.model.Usuarios
import com.appgestion.gestionempresa.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : AuthRepository {

    override suspend fun registerUser(
        email: String,
        password: String,
        tipe: String,
        name: String,
        phone: String
    ): Response<Boolean> {

        return try {

            val result = auth.createUserWithEmailAndPassword(email, password).await()
            Log.d("AuthRepo", "Usuario creado: ${result.user?.uid}")
            val userUid =
                result?.user?.uid ?: return Response.Failure(Exception("Uid no encontrado"))

            var usuario = Usuarios(
                uid = userUid,
                email = email,
                tipo = tipe
            )
            if(name.isNotBlank()) usuario = usuario.copy(name = name)
            if(phone.isNotBlank()) usuario = usuario.copy(phone = phone)

            firestore.collection("usuarios").document(userUid).set(usuario).await()
            return Response.Success(true)
        } catch (e: Exception) {
            return Response.Failure(e)
        }
    }

    override suspend fun loginUser(email: String, password: String): Response<String> {

        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()

            val userUid = result.user?.uid
                ?: return Response.Failure(Exception("uid no encontrado"))

            Log.d("AUTHRepo", "Usuario logueado: $userUid")

            Response.Success(userUid)
        }catch (e: Exception){
            Log.w("AuthRepo", "loginUser: failure", e)
            Response.Failure(e)
        }


    }

    override suspend fun fetchUser(uid: String): Response<Usuarios> {
        return try {
            val usuarios = firestore.collection("usuarios").document(uid).get().await()
            if(!usuarios.exists()){
                return Response.Failure(Exception("Usuario no encontrado"))
            }

            val user = usuarios.toObject(Usuarios::class.java)
                ?: return Response.Failure(Exception("Fallo al cargar usuario"))

            Response.Success(user)

        }catch (e: Exception){
            Response.Failure(e)
        }


    }

    override suspend fun recuperarPassword(email: String) {
        TODO("Not yet implemented")
    }

}
