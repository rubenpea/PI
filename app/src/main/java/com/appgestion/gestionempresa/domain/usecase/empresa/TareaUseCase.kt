package com.appgestion.gestionempresa.domain.usecase.empresa

import com.appgestion.gestionempresa.data.model.Response
import com.appgestion.gestionempresa.domain.model.EstadoTarea
import com.appgestion.gestionempresa.domain.model.TareaEntity
import com.appgestion.gestionempresa.domain.repository.TareaRepository
import javax.inject.Inject

class TareaUseCases @Inject constructor(
    private val repository: TareaRepository
) {

    suspend fun crearTarea(tarea: TareaEntity): Response<Boolean> {
        return repository.crearTarea(tarea)
    }

    suspend fun obtenerTareasEmpresa(idEmpresa: String): Response<List<TareaEntity>> {
        return repository.obtenerTareasEmpresa(idEmpresa)
    }

    suspend fun obtenerTareasTrabajador(idTrabajador: String): Response<List<TareaEntity>> {
        return repository.obtenerTareasTrabajador(idTrabajador)
    }

    suspend fun actualizarEstado(idTarea: String, estado: EstadoTarea): Response<Boolean> {
        return repository.actualizarEstadoTarea(idTarea, estado)
    }
}
