package com.appgestion.gestionempresa.domain.repository

import com.appgestion.gestionempresa.data.model.Response
import com.appgestion.gestionempresa.domain.model.VacacionesEntity

interface VacacionesRepository {
    suspend fun submitRequest(req: VacacionesEntity): Response<Boolean>
    suspend fun getMyRequests(workerId: String): Response<List<VacacionesEntity>>
    suspend fun getRequestsByEmpresa(empresaId: String): Response<List<VacacionesEntity>>
    suspend fun updateStatus(id: String, newStatus: String): Response<Boolean>
}