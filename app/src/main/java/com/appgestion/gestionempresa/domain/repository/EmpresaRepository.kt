package com.appgestion.gestionempresa.domain.repository

import com.appgestion.gestionempresa.data.model.Response
import com.appgestion.gestionempresa.domain.model.EmpresaEntity
import com.appgestion.gestionempresa.domain.model.UsuarioEntity


interface EmpresaRepository {

    /**
     * Crea o actualiza el perfil de la empresa en Firestore.
     * Si el documento no existe, lo crea; si existe, lo actualiza.
     */
    suspend fun saveEmpresa(empresa: EmpresaEntity): Response<Boolean>

    /**
     * Obtiene la entidad EmpresaEntity a partir de su UID.
     */
    suspend fun fetchEmpresa(uid: String): Response<EmpresaEntity>

    /**
     * Añade un trabajador (uidTrabajador) a la lista de listaTrabajadores de esta empresa.
     */
    suspend fun findTrabajadorIdByEmail(email: String): Response<String>
    suspend fun addTrabajadorToEmpresa(idEmpresa: String, uidUsuario: String): Response<Unit>

    /**
     * Elimina un trabajador de la lista.
     */
    suspend fun removeTrabajador(empUid: String, trabUid: String): Response<Boolean>

    suspend fun obtenerTrabajadoresDeEmpresa(empresaId: String): List<UsuarioEntity>

    suspend fun asignarEmpresaAlTrabajador(trabajadorId: String, empresaId: String): Response<Unit>

    suspend fun getEmpresaById(id: String): Response<EmpresaEntity>


}
