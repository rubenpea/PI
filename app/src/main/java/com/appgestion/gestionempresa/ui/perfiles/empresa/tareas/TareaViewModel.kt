package com.appgestion.gestionempresa.ui.perfiles.empresa.tareas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appgestion.gestionempresa.data.model.Response
import com.appgestion.gestionempresa.domain.model.EstadoTarea
import com.appgestion.gestionempresa.domain.model.TareaEntity
import com.appgestion.gestionempresa.domain.usecase.empresa.TareaUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject


@HiltViewModel
class TareaViewModel @Inject constructor(
    private val tareaUseCases: TareaUseCases
) : ViewModel() {

    private val _tareas =
        MutableStateFlow<Response<List<TareaEntity>>>(Response.Success(emptyList()))
    val tareas: StateFlow<Response<List<TareaEntity>>> = _tareas

    fun crearTarea(titulo: String, descripcion: String, trabajadorId: String, empresaId: String) {
        val tarea = TareaEntity(
            titulo = titulo,
            descripcion = descripcion,
            fechaCreacion = Date().time,
            estado = EstadoTarea.PENDIENTE,
            trabajadorId = trabajadorId,
            empresaId = empresaId
        )
        viewModelScope.launch {
            tareaUseCases.crearTarea(tarea)
        }
    }

    fun cargarTareasEmpresa(idEmpresa: String) {
        viewModelScope.launch {
            _tareas.value = Response.Loading
            _tareas.value = tareaUseCases.obtenerTareasEmpresa(idEmpresa)
        }
    }

    fun cargarTareasTrabajador(idTrabajador: String) {
        viewModelScope.launch {
            _tareas.value = tareaUseCases.obtenerTareasTrabajador(idTrabajador)
        }
    }

    fun actualizarEstadoTarea(idTarea: String, nuevoEstado: EstadoTarea) {
        viewModelScope.launch {
            tareaUseCases.actualizarEstado(idTarea, nuevoEstado)
        }
    }


}


