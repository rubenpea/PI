package com.appgestion.gestionempresa.ui.perfiles.empresa.ofertas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appgestion.gestionempresa.data.model.Response
import com.appgestion.gestionempresa.domain.model.CurriculumEntity
import com.appgestion.gestionempresa.domain.model.OfertaEntity
import com.appgestion.gestionempresa.domain.usecase.empresa.CreateOfertaUseCase
import com.appgestion.gestionempresa.domain.usecase.empresa.DeleteOfertaUseCase
import com.appgestion.gestionempresa.domain.usecase.empresa.GetOfertasByEmpresaUseCase
import com.appgestion.gestionempresa.domain.usecase.trabajador.GetMyCVsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OfertaViewModel @Inject constructor(
    private val createOfertaUseCase: CreateOfertaUseCase,
    private val getByEmpresaUseCase: GetOfertasByEmpresaUseCase,
    private val deleteOfertaUseCase: DeleteOfertaUseCase,
    private val getMyCVs: GetMyCVsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(OfertaState())
    val uiState: StateFlow<OfertaState> = _uiState.asStateFlow()

    private val _cvs = MutableStateFlow<Response<List<CurriculumEntity>>>(Response.Loading)
    val cvs: StateFlow<Response<List<CurriculumEntity>>> = _cvs



    fun onTituloChange(value: String)     = _uiState.update { it.copy(titulo      = value) }
    fun onDescripcionChange(value: String)= _uiState.update { it.copy(descripcion = value) }
    fun onCategoriaChange(value: String)  = _uiState.update { it.copy(categoria   = value) }
    fun onCaducidadChange(value: Long?) = _uiState.update { it.copy(fechaCaducidad = value) }
    fun onSalarioChange(value: Double)     = _uiState.update { it.copy(salario     = value) }

    fun loadOfertas(empresaId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(ofertasResponse = Response.Loading) }

            val res = getByEmpresaUseCase(empresaId)
            _uiState.update { it.copy(ofertasResponse = res) }
        }
    }

    fun publicarOferta(empresaId: String) {
        viewModelScope.launch {
            val oferta = OfertaEntity(
                id = "",
                idEmpresa        = empresaId,
                titulo           = uiState.value.titulo,
                descripcion      = uiState.value.descripcion,
                requisitos       = uiState.value.categoria ?: "",
                salario          = uiState.value.salario,
                fechaPublicacion = System.currentTimeMillis(),
                fechaCaducidad   = uiState.value.fechaCaducidad
            )
            when (val res = createOfertaUseCase(oferta)) {
                is Response.Success -> loadOfertas(empresaId)
                is Response.Failure ->
                    _uiState.update { it.copy(ofertasResponse = Response.Failure(res.exception)) }
                is Response.Loading ->
                    _uiState.update { it.copy(ofertasResponse = Response.Loading) }
            }
        }
    }
    fun deleteOferta(ofertaId: String, empresaId: String) {
        viewModelScope.launch {
            when(val res = deleteOfertaUseCase(ofertaId)) {
                is Response.Success -> loadOfertas(empresaId)
                is Response.Failure ->
                    _uiState.update { it.copy(ofertasResponse = Response.Failure(res.exception)) }
                else -> Unit
            }
        }
    }

    fun loadMyCVs(workerId: String) = viewModelScope.launch {
        _cvs.value = Response.Loading
        _cvs.value = getMyCVs(workerId)
    }
}
