package com.appgestion.gestionempresa.data.repository

import android.net.Uri
import com.appgestion.gestionempresa.data.model.FacturaDto
import com.appgestion.gestionempresa.data.model.Response
import com.appgestion.gestionempresa.data.model.toDomain
import com.appgestion.gestionempresa.domain.model.FacturaEntity
import com.appgestion.gestionempresa.domain.repository.FacturaRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

class FacturaRepositoryImpl(
    private val storage: FirebaseStorage,
    private val firestore: FirebaseFirestore
) : FacturaRepository {

    override suspend fun subirFactura(uri: Uri, idEmpresa: String): Response<String> {
        return try {
            val path = "facturas/$idEmpresa/${System.currentTimeMillis()}.jpg"
            val ref = storage.reference.child(path)

            ref.putFile(uri).await()
            val downloadUrl = ref.downloadUrl.await()

            val facturaData = FacturaDto(
                empresaId = idEmpresa,
                url = downloadUrl.toString(),
                timestamp = System.currentTimeMillis()
            )

            firestore.collection("facturas").add(facturaData).await()

            Response.Success(downloadUrl.toString())
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    override suspend fun obtenerFacturas(idEmpresa: String): Response<List<FacturaEntity>> {
        return try {
            val snapshot = firestore.collection("facturas")
                .whereEqualTo("empresaId", idEmpresa)
                .get()
                .await()

            val facturas = snapshot.documents.mapNotNull { doc ->
                doc.toObject(FacturaDto::class.java)?.toDomain(doc.id)
            }

            Response.Success(facturas)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }
}