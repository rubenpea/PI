package com.appgestion.gestionempresa.domain.repository

import com.appgestion.gestionempresa.domain.model.EstadoTarea


import com.appgestion.gestionempresa.data.model.Response
import com.appgestion.gestionempresa.domain.model.TareaEntity

interface TareaRepository {
    suspend fun crearTarea(tarea: TareaEntity): Response<Boolean>
    suspend fun obtenerTareasEmpresa(idEmpresa: String): Response<List<TareaEntity>>
    suspend fun obtenerTareasTrabajador(idTrabajador: String): Response<List<TareaEntity>>
    suspend fun actualizarEstadoTarea(idTarea: String, nuevoEstado: EstadoTarea): Response<Boolean>
}