package com.appgestion.gestionempresa.domain.repository

import com.appgestion.gestionempresa.data.model.Response
import com.appgestion.gestionempresa.domain.model.CandidaturaEntity
import com.appgestion.gestionempresa.domain.model.UsuarioEntity

interface CandidaturaRepository {
    suspend fun getCandidaturasByTrabajador(trabajadorId: String): Response<List<CandidaturaEntity>>
    suspend fun getCandidaturasByOferta(ofertaId: String): Response<List<CandidaturaEntity>>
    suspend fun updateStatus(candidaturaId: String, status: String): Response<Boolean>
    suspend fun sendCandidatura(
        trabajadorId: String,
        ofertaId: String,
        cvId: String
    ): Response<Boolean>

    suspend fun getUsuarioById(id: String): Response<UsuarioEntity>

}