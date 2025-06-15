package com.appgestion.gestionempresa.domain.repository

import android.net.Uri
import com.appgestion.gestionempresa.data.model.Response
import com.appgestion.gestionempresa.domain.model.FacturaEntity

interface FacturaRepository {
    suspend fun subirFactura(uri: Uri, idEmpresa: String): Response<String>
    suspend fun obtenerFacturas(idEmpresa: String): Response<List<FacturaEntity>>

}