package com.appgestion.gestionempresa.ui.perfiles.empresa.facturas

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appgestion.gestionempresa.data.model.Response
import com.appgestion.gestionempresa.domain.model.FacturaEntity
import com.appgestion.gestionempresa.domain.repository.FacturaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FacturaViewModel @Inject constructor(
    private val facturaRepository: FacturaRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<Response<String>>(Response.Success(""))
    val uidState: StateFlow<Response<String>> = _uiState

    private val _listaFacturas = MutableStateFlow<Response<List<FacturaEntity>>>(Response.Success(emptyList()))
    val listaFacturas: StateFlow<Response<List<FacturaEntity>>> = _listaFacturas

    fun resetUploadState() {
        _uiState.value = Response.Success("")
    }


    fun subirFactura(imageUri: Uri, idEmpresa: String) {
        viewModelScope.launch {
            _uiState.value = Response.Success("") // reset
            _uiState.value = Response.Failure(Exception("Cargando"))

            val result = facturaRepository.subirFactura(imageUri, idEmpresa)
            _uiState.value = result
        }
    }

    fun cargarFacturas(idEmpresa: String) {
        viewModelScope.launch {
            _listaFacturas.value = Response.Failure(Exception("Cargando"))
            _listaFacturas.value = facturaRepository.obtenerFacturas(idEmpresa)
        }
    }
}
