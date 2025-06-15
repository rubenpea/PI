package com.appgestion.gestionempresa.domain.repository

import com.appgestion.gestionempresa.data.model.Response
import com.appgestion.gestionempresa.domain.model.EmpresaEntity
import com.appgestion.gestionempresa.domain.model.OfertaEntity
import com.appgestion.gestionempresa.domain.model.UsuarioEntity

interface OfertaRepository {
    suspend fun crearOferta(oferta: OfertaEntity): Response<Boolean>
    suspend fun eliminarOferta(ofertaId: String): Response<Boolean>
    suspend fun obtenerOfertasPorEmpresa(empresaId: String): Response<List<OfertaEntity>>
    suspend fun getAllOfertas(): Response<List<OfertaEntity>>
    suspend fun getOfertaById(id: String): Response<OfertaEntity>


}