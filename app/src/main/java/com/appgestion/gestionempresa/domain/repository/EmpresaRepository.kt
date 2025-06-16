package com.appgestion.gestionempresa.domain.repository

import com.appgestion.gestionempresa.data.model.Response
import com.appgestion.gestionempresa.domain.model.EmpresaEntity
import com.appgestion.gestionempresa.domain.model.UsuarioEntity


interface EmpresaRepository {

    suspend fun saveEmpresa(empresa: EmpresaEntity): Response<Boolean>

    suspend fun fetchEmpresa(uid: String): Response<EmpresaEntity>

    suspend fun findTrabajadorIdByEmail(email: String): Response<String>

    suspend fun addTrabajadorToEmpresa(idEmpresa: String, uidUsuario: String): Response<Unit>

    suspend fun removeTrabajador(empUid: String, trabUid: String): Response<Boolean>

    suspend fun obtenerTrabajadoresDeEmpresa(empresaId: String): List<UsuarioEntity>

    suspend fun asignarEmpresaAlTrabajador(trabajadorId: String, empresaId: String): Response<Unit>

    suspend fun getEmpresaById(id: String): Response<EmpresaEntity>


}
